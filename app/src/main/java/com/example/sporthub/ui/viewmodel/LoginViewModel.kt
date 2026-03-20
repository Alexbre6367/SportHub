package com.example.sporthub.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sporthub.data.repository.SportHubRepository
import com.example.sporthub.data.sporthub.SportHubDatabase
import com.example.sporthub.data.sporthub.User
import com.example.sporthub.util.SecureStorage
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(application: Application) : AndroidViewModel(application){
    private val db = SportHubDatabase.getInstance(application)
    private val repository = SportHubRepository(db.sportHubDao, db.healthDao, db.workoutDao)
    private val secureStorage = SecureStorage.getInstance(application)
    private val auth = FirebaseAuth.getInstance()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()
    private val firestore = FirebaseFirestore.getInstance()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isUpdateVersion = MutableStateFlow(false)
    val isUpdateVersion = _isUpdateVersion.asStateFlow()

    private val _isDelete = MutableStateFlow(false)
    val isDelete = _isDelete.asStateFlow()

    private val _isResetPassword = MutableStateFlow(false)
    val isResetPassword = _isResetPassword.asStateFlow()

    private val _loadedBitmap = MutableStateFlow<Bitmap?>(null)
    val loadedBitmap = _loadedBitmap.asStateFlow()



    init { //данные доступны после создания viewModel(все переменные выше)
        loadUserData()

        viewModelScope.launch {
            currentUser.collect { user ->
                user?.uri?.let {
                    if (user.uri.isNotEmpty()) {
                        loadBitmapFromUri(user.uri.toUri())
                    } else {
                        _loadedBitmap.value = null
                    }
                }
            }
        }
    }

    private suspend fun syncUserToFirestore(user: User) {
        val userMap = hashMapOf(
            "userId" to user.userId,
            "level" to user.level,
            "name" to user.name,
            "gender" to user.gender,
            "weight" to user.weight,
            "height" to user.height,
            "birthdate" to user.birthdate,
            "version" to user.version,
            "select" to user.select,
        )

        firestore.collection("users")
            .document(user.userId)
            .set(userMap)
            .await()

        Log.d("MyLog", "Данные синхронизированы с Firestore для userId: ${user.userId}")
    }

    private suspend fun loadUserFromFirestore(userId: String): User? {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            if(document.exists()) {
                User(
                    userId = document.getString("userId") ?: userId,
                    level = document.getLong("level")?.toInt() ?: 0,
                    name = document.getString("name") ?: "",
                    gender = document.getString("gender") ?: "",
                    weight = document.getDouble("weight")?.toFloat() ?: 0f,
                    height = document.getLong("height")?.toInt() ?: 0,
                    birthdate = document.getLong("birthdate") ?: 0L,
                    version = document.getLong("version")?.toInt() ?: 0,
                    select = document.getBoolean("select") ?: false,
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка загрузки из Firestore", e)
            null
        }
    }
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user

                if (user != null) {
                    val firestoreUser = loadUserFromFirestore(user.uid)

                    if(firestoreUser != null) {
                        secureStorage.saveUserId(user.uid)
                        val localUser = repository.getUser(user.uid)
                        val userToSave = firestoreUser.copy(uri = localUser?.uri ?: "")
                        repository.addUser(userToSave)
                        _authState.value = AuthState.Success
                        Log.d("MyLog", "Данные загружены из Firestore")
                    } else {
                        auth.signOut()
                    }

                    _authState.value = AuthState.Success
                    Log.d("MyLog", "Пользователь вошёл $email")
                } else {
                   Log.e("MyLog", "Не удалось войти")
                }
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка входа", e)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun signUp(email: String, password: String) {
        if(!isValidEmail(email)) {
            return
        }
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user

                if(user != null) {
                    val newUser = User(
                        userId = user.uid
                    )
                    syncUserToFirestore(newUser)
                    repository.addUser(newUser)
                    secureStorage.saveUserId(user.uid)
                    _authState.value = AuthState.Success
                    Log.d("MyLog", "Пользователь зарегистрирован $email")
                } else {
                    Log.e("MyLog", "Не удалось зарегистрироваться")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error
                Log.e("MyLog", "Ошибка регистрации", e)
            }
        }
    }

    fun levelUser(level: Int) {
        viewModelScope.launch {
            val uid = secureStorage.getUserId() ?: return@launch

            val currentUser = repository.getUser(uid)
            if (currentUser != null) {
                val updatedUser = currentUser.copy(level = level)
                repository.updateUser(updatedUser)
                syncUserToFirestore(updatedUser)
                Log.d("MyLog", "Уровень сохранен: $level")
            }
        }
    }

    fun detailsUser(name: String, gender: String, weight: Float, height: Int, birthdate: Long) {
        viewModelScope.launch {
            val uid = secureStorage.getUserId() ?: return@launch

            val currentUser = repository.getUser(uid)
            if (currentUser != null) {
                val updatedUser = currentUser.copy(
                    name = name,
                    gender = gender,
                    weight = weight,
                    height = height,
                    birthdate = birthdate
                )
                repository.updateUser(updatedUser)
                syncUserToFirestore(updatedUser)
                Log.d("MyLog", "Все данные сохранены")
            }
        }
    }

    fun version(selectedIndex: Int) {
        viewModelScope.launch {
            _isUpdateVersion.value = true
            val uid = secureStorage.getUserId() ?: return@launch

            val currentUser = repository.getUser(uid) ?: User(userId = uid)
            val updatedUser = currentUser.copy(version = selectedIndex)
            repository.updateUser(updatedUser)
            syncUserToFirestore(updatedUser)
            Log.d("MyLog", "Версия сохранена: $selectedIndex для пользователя $uid")
            loadUserData()
            _isUpdateVersion.value = false
        }
    }


    suspend fun getStartScreen(): String {
        val firebaseUser = auth.currentUser ?: return "welcome_screen"
        val userId = firebaseUser.uid
        var user = repository.getUser(userId)

        if (user == null) {
            val firestoreUser = loadUserFromFirestore(userId)
            if(firestoreUser != null) {
                repository.addUser(firestoreUser)
                user = firestoreUser
                Log.d("MyLog", "Пользователь востановлен из Firestore")
            } else {
                auth.signOut()
            }
        }

        if (user == null) return "level_screen"

        if(user.level == 0) return "level_screen"

        val hasPersonalDetails = user.name.isNotEmpty() && user.gender.isNotEmpty() &&
                user.weight > 0f &&
                user.height > 0 &&
                user.birthdate > 0L

        if(!hasPersonalDetails) return "details_up"

        if(user.version == 0) return "start_screen"

        Log.d("MyLog", "Все данные заполнены")
        return "home_screen"
    }

    fun loadUserData() {
        viewModelScope.launch {
            val uid = secureStorage.getUserId()
            if(uid != null) {
                _currentUser.value = repository.getUser(uid)
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val context = getApplication<Application>()
            val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
            val file = java.io.File(context.filesDir, fileName)

            val source = ImageDecoder.createSource(context.contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.setTargetSampleSize(2)
            }

            file.outputStream().use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, output)
            }

            fileName
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка сохранения фото", e)
            null
        }
    }

    private fun loadBitmapFromUri(uri: Uri) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>()

                val actualUri = if (uri.scheme == null) {
                    val file = java.io.File(context.filesDir, uri.toString())
                    Uri.fromFile(file)
                } else {
                    uri
                }

                val source = ImageDecoder.createSource(context.contentResolver, actualUri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                _loadedBitmap.value = bitmap

                Log.d("MyLog", "Bitmap загружен из: $actualUri")
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка загрузки Bitmap", e)
            }
        }
    }

    fun imageUri(uri: Uri) {
        viewModelScope.launch {
            try {
                val uid = secureStorage.getUserId() ?: return@launch
                val userFromDb = repository.getUser(uid) ?: return@launch

                if (!userFromDb.uri.isNullOrEmpty()) {
                    deleteOldImage(userFromDb.uri)
                }

                val fileName = saveImageToInternalStorage(uri)

                if (fileName != null) {
                    val updatedUser = userFromDb.copy(uri = fileName)
                    repository.updateUser(updatedUser)
                    _currentUser.value = updatedUser

                    loadBitmapFromUri(fileName.toUri())

                    Log.d("MyLog", "Изображение обновлено: $fileName")
                }
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка обновления изображения", e)
            }
        }
    }

    private fun deleteOldImage(fileName: String) {
        try {
            val context = getApplication<Application>()
            val file = java.io.File(context.filesDir, fileName)
            if (file.exists()) {
                file.delete()
                Log.d("MyLog", "Старое изображение удалено")
            }
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка удаления старого изображения", e)
        }
    }

    fun selectionWorkout() {
        viewModelScope.launch {
            val uid = secureStorage.getUserId() ?: return@launch
            val currentUser = repository.getUser(uid)
            if(currentUser != null) {
                val updatedUser = currentUser.copy(select = true)
                repository.updateUser(updatedUser)
                syncUserToFirestore(updatedUser)
                _currentUser.value = updatedUser
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                auth.signOut()
                secureStorage.saveUserId("")
                _currentUser.value = null
                _authState.value = AuthState.Idle
                Log.d("MyLog", "Пользователь вышел из аккаунта")
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка выхода", e)
            }
        }
    }

    fun deleteAccount(password: String, onSuccess: () -> Unit) {
        val user = auth.currentUser ?: return
        val email = user.email ?: return
        val credential = EmailAuthProvider.getCredential(email, password)

        _isDelete.value = true
        _authState.value = AuthState.Loading
        user.reauthenticate(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModelScope.launch {
                    try {
                        repository.deleteUser(user.uid)
                        user.delete().await()
                        secureStorage.clearUserId()
                        _currentUser.value = null
                        _authState.value = AuthState.Idle
                        onSuccess()
                    } catch (e: Exception) {
                        _isDelete.value = false
                        Log.e("MyLog", "Ошибка удаления аккаунта", e)
                    }
                }
            } else {
                _isDelete.value = false
            }
        }
    }

    fun resetPassword(context: Context, email: String? = null) {
        val targetEmail = if(!email.isNullOrBlank()) {
            email
        } else {
            auth.currentUser?.email
        }

        if(targetEmail.isNullOrBlank()) return
        _isResetPassword.value = true
        viewModelScope.launch {
            auth.sendPasswordResetEmail(targetEmail).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Password reset email sent to $targetEmail",
                        Toast.LENGTH_LONG
                    ).show()
                    _isResetPassword.value = false
                } else {
                    Toast.makeText(
                        context,
                        "Error sending email",
                        Toast.LENGTH_LONG
                    ).show()
                    _isResetPassword.value = false
                }
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object Error : AuthState()
}
