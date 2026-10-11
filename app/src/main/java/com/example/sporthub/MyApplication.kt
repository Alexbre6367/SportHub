package com.example.sporthub

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.auth

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Log.d("MyLog", "Firebase инициализирован")

        val firebaseAppCheck = Firebase.appCheck
        firebaseAppCheck.installAppCheckProviderFactory(
            if(BuildConfig.DEBUG) DebugAppCheckProviderFactory.getInstance()
            else PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        if(Firebase.auth.currentUser == null) {
            Firebase.auth.signInAnonymously().addOnFailureListener { Log.e("MyLog", "Ошибка вхожа ${it.message}") }
        } else {
            Log.d("MyLog", "Пользователь уже авторизован:: ${Firebase.auth.currentUser?.uid}")
        }
    }
}