package com.example.sporthub.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sporthub.data.auth.GoogleAuth
import com.example.sporthub.data.auth.SecureStorage
import com.example.sporthub.data.repository.AuthRepository
import com.example.sporthub.data.repository.SportHubRepository
import com.example.sporthub.data.sporthub.SportHubDatabase
import com.example.sporthub.data.sporthub.User
import com.example.sporthub.utils.toBitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

class LoginViewModel(application: Application) : AndroidViewModel(application){
    private val db = SportHubDatabase.getInstance(application)
    private val sportHubRepository = SportHubRepository(db.sportHubDao, db.healthDao, db.workoutDao)
    private val authRepository = AuthRepository(
        FirebaseAuth.getInstance(),
        FirebaseFirestore.getInstance(),
        sportHubRepository,
        SecureStorage.getInstance(application)
    )

    private val googleAuth = GoogleAuth(application)
    private val secureStorage = SecureStorage.getInstance(application)
    private val auth = FirebaseAuth.getInstance()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()
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

    private val _isGoogleAccount = MutableStateFlow(false)
    val isGoogleAccount = _isGoogleAccount.asStateFlow()


    init {
        loadUserData()
        _isGoogleAccount.value = authRepository.isGoogleUser()

        viewModelScope.launch {
            currentUser.collect { user ->
                _loadedBitmap.value = user?.uri?.takeIf {
                    it.isNotEmpty()
                }?.toUri()?.toBitmap(application)
            }
        }
    }

    fun loadUserData() {
        viewModelScope.launch {
            val uid = secureStorage.getUserId()
            if(uid != null) {
                sportHubRepository.getUser(uid).collect { user ->
                    _currentUser.value = user
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = authRepository.signInEmail(email, password)
                if(user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Success
                    Log.e("MyLog", "Пользователь авторизовался через Email $email")
                } else {
                    _authState.value = AuthState.Error
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error
            }
        }
    }

    fun signInGoogle() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val idToken = googleAuth.getGoogleIdToken()

            if(idToken != null) {
                try {
                    val user = authRepository.signInGoogle(idToken)
                    if(user != null) {
                        _currentUser.value = user
                        _authState.value = AuthState.Success
                        _isGoogleAccount.value = false
                        Log.e("MyLog", "Пользователь авторизовался через Google")
                    } else {
                        _authState.value = AuthState.Error
                    }
                } catch (e: Exception) {
                    _authState.value = AuthState.Error
                }
            } else {
                _authState.value = AuthState.Error
            }
        }
    }


    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = authRepository.signUpEmail(email, password)
                if (user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error
            }
        }
    }

    fun levelUser(level: Int) {
        viewModelScope.launch {
            val uid = secureStorage.getUserId() ?: return@launch

            val currentUser = sportHubRepository.getUser(uid).first()
            if (currentUser != null) {
                val updatedUser = currentUser.copy(level = level)
                sportHubRepository.updateUser(updatedUser)
                authRepository.syncUserToFirestore(updatedUser)
                Log.d("MyLog", "Уровень сохранен: $level")
            }
        }
    }

    fun detailsUser(name: String, gender: String, weight: Int, height: Int, birthdate: Long) {
        viewModelScope.launch {
            val uid = secureStorage.getUserId() ?: return@launch

            val currentUser = sportHubRepository.getUser(uid).first()
            if (currentUser != null) {
                val updatedUser = currentUser.copy(
                    name = name,
                    gender = gender,
                    weight = weight,
                    height = height,
                    birthdate = birthdate
                )
                sportHubRepository.updateUser(updatedUser)
                authRepository.syncUserToFirestore(updatedUser)
                Log.d("MyLog", "Все данные сохранены")
            }
        }
    }

    fun version(selectedIndex: Int) {
        viewModelScope.launch {
            _isUpdateVersion.value = true
            val uid = secureStorage.getUserId() ?: return@launch

            val currentUser = sportHubRepository.getUser(uid).first() ?: User(userId = uid, date = authRepository.getDayId(Calendar.getInstance()))
            val updatedUser = currentUser.copy(version = selectedIndex)
            sportHubRepository.updateUser(updatedUser)
            authRepository.syncUserToFirestore(updatedUser)
            Log.d("MyLog", "Версия сохранена: $selectedIndex для пользователя $uid")
            loadUserData()
            _isUpdateVersion.value = false
        }
    }


    suspend fun getStartScreen(): String {
        val firebaseUser = auth.currentUser ?: return "welcome_screen"
        val userId = firebaseUser.uid
        var user = sportHubRepository.getUser(userId).first()

        if (user == null) {
            val firestoreUser = authRepository.loadUserFromFirestore(userId)
            if(firestoreUser != null) {
                sportHubRepository.addUser(firestoreUser)
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
                user.birthdate != 0L

        if(!hasPersonalDetails) return "details_up"

        if(user.version == 0) return "start_screen"

        Log.d("MyLog", "Все данные заполнены")
        return "home_screen/0"
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val context = getApplication<Application>()
            val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

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

    fun imageUri(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val uid = secureStorage.getUserId() ?: return@launch
                val userFromDb = sportHubRepository.getUser(uid).first() ?: return@launch

                if (!userFromDb.uri.isNullOrEmpty()) {
                    deleteOldImage(userFromDb.uri)
                }

                val fileName = saveImageToInternalStorage(uri)

                if (fileName != null) {
                    val updatedUser = userFromDb.copy(uri = fileName)
                    sportHubRepository.updateUser(updatedUser)
                    _currentUser.value = updatedUser

                    uri.toBitmap(context)

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
            val file = File(context.filesDir, fileName)
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
            val currentUser = sportHubRepository.getUser(uid).first()
            if(currentUser != null) {
                val updatedUser = currentUser.copy(select = true)
                sportHubRepository.updateUser(updatedUser)
                authRepository.syncUserToFirestore(updatedUser)
                _currentUser.value = updatedUser
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            googleAuth.signOut()

            _isGoogleAccount.value = false
            _currentUser.value = null
            _authState.value = AuthState.Idle
            Log.d("MyLog", "Пользователь вышел из аккаунта")
        }
    }

    fun deleteAccount(password: String?, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            _isDelete.value = true
            _authState.value = AuthState.Loading

            val isDelete = if (password != null) {
                authRepository.deleteAccountEmail(password)
            } else {
                val idToken = googleAuth.getGoogleIdToken()
                if (idToken != null) {
                    authRepository.deleteAccountGoogle(idToken)
                } else {
                    false
                }
            }

            if(isDelete) {
                _currentUser.value = null
                _authState.value = AuthState.Idle
                onSuccess()
                Log.d("MyLog", "Пользователь удален")
            } else {
                _authState.value = AuthState.Error
                onError()
            }

            _isDelete.value = false
        }
    }

    fun resetPassword(context: Context, email: String? = null, onSuccess: () -> Unit) {
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
                    onSuccess()
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
