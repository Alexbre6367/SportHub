package com.example.sporthub.data.repository

import android.util.Log
import android.util.Patterns
import com.example.sporthub.data.auth.SecureStorage
import com.example.sporthub.data.sporthub.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class AuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val sportHubRepository: SportHubRepository,
    private val secureStorage: SecureStorage
) {

    fun isGoogleUser(): Boolean {
        val user = auth.currentUser ?: return false
        return user.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }
    }
    private suspend fun handleSignInUp(user: String): User {
        val existingUser = loadUserFromFirestore(user)
        val newUser = existingUser ?: User(
            userId = user,
            date = getDayId(Calendar.getInstance())
        )

        if(existingUser == null) {
            syncUserToFirestore(newUser)
        }

        sportHubRepository.addUser(newUser)
        secureStorage.saveUserId(user)
        return newUser
    }

    suspend fun signInEmail(email: String, password: String): User? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user?.uid ?: ""
            handleSignInUp(user)
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка входа", e)
            null
        }
    }

    suspend fun signUpEmail(email: String, password: String): User? {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.d("MyLog", "Ошибка email $email")
            return null
        }

        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user?.uid ?: ""
            handleSignInUp(user)
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка регистрации", e)
            null
        }
    }

    suspend fun signInGoogle(idToken: String): User? {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()

            val user = result.user?.uid
            if(user != null) {
                handleSignInUp(user)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка входа Google", e)
            null
        }
    }

    suspend fun deleteAccountEmail(password: String): Boolean {
        val email = auth.currentUser?.email ?: return false
        val credential = EmailAuthProvider.getCredential(email, password)
        return performDeleteAccount(credential)
    }

    suspend fun deleteAccountGoogle(idToken: String): Boolean {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return performDeleteAccount(credential)
    }

    private suspend fun performDeleteAccount(credential: AuthCredential): Boolean {
        return try {
            val user = auth.currentUser ?: return false
            user.reauthenticate(credential).await()
            sportHubRepository.deleteUser(user.uid)
            user.delete().await()
            secureStorage.clearUserId()
            true
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка удаления аккаунта", e)
            false
        }
    }

    suspend fun syncUserToFirestore(user: User) {
        val userMap = hashMapOf(
            "userId" to user.userId,
            "date" to user.date,
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

    suspend fun loadUserFromFirestore(userId: String): User? {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            if(document.exists()) {
                User(
                    userId = document.getString("userId") ?: userId,
                    date = document.getLong("date") ?: getDayId(Calendar.getInstance()),
                    level = document.getLong("level")?.toInt() ?: 0,
                    name = document.getString("name") ?: "",
                    gender = document.getString("gender") ?: "",
                    weight = document.getDouble("weight")?.toInt() ?: 0,
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

    fun signOut() {
        try {
            auth.signOut()
            secureStorage.saveUserId("")
            Log.d("MyLog", "Пользователь вышел из аккаунта")
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка выхода", e)
        }
    }

    fun getDayId(cal: Calendar): Long {
        return (cal.get(Calendar.YEAR) * 10000 +
                (cal.get(Calendar.MONTH) + 1) * 100 +
                cal.get(Calendar.DAY_OF_MONTH)).toLong()
    }
}