package com.example.sporthub.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.icu.util.Calendar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sporthub.data.auth.SecureStorage
import com.example.sporthub.data.repository.SportHubRepository
import com.example.sporthub.data.sporthub.SportHubDatabase
import com.example.sporthub.llm.Gemini
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val image: Bitmap? = null
)

class GeminiViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SportHubDatabase.getInstance(application)
    private val repository = SportHubRepository(db.sportHubDao, db.healthDao, db.workoutDao, db.faceDao)
    private val secureStorage = SecureStorage.getInstance(application)

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val message = _messages.asStateFlow()
    private val _chatHistory = mutableListOf<Content>()
    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loadedBitmap = MutableStateFlow<Bitmap?>(null)
    val loadedBitmap = _loadedBitmap.asStateFlow()

    fun message(text: String) {
        val bitmap = _loadedBitmap.value
        clearFile()
        _messages.value += ChatMessage(text, true, bitmap)
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = secureStorage.getUserId() ?: return@launch
                val user = repository.getUser(userId).first() ?: return@launch

                val cal = Calendar.getInstance()
                val dateId = (cal.get(java.util.Calendar.YEAR) * 10000 +
                        (cal.get(java.util.Calendar.MONTH) + 1) * 100 +
                        cal.get(java.util.Calendar.DAY_OF_MONTH)).toLong()

                val healthData = repository.getHealthForToday(dateId).first() ?: return@launch

                val faceData = repository.getFaceData.first() ?: return@launch

                val aiResponse = if(bitmap != null) {
                    Gemini.analyzePhoto(
                        text,
                        bitmap,
                        _chatHistory,
                        user,
                        healthData,
                        faceData,
                    )
                } else {
                    Gemini.analyze(
                        text,
                        _chatHistory,
                        user,
                        healthData,
                        faceData,
                    )
                }

                if(aiResponse != null) {
                    _chatHistory.add(content(role = "user") {
                        if(bitmap != null) image(bitmap)
                        text(text)
                    })
                    _chatHistory.add(content(role = "model") { text(aiResponse) })

                    _messages.value += ChatMessage(aiResponse, false)
                    _loadedBitmap.value = null
                } else {
                    _messages.value += ChatMessage("Ошибка обработки", false)
                }
            } catch (e: Exception) {
                _messages.value += ChatMessage(
                    text = "Ошибка обработки ${e.localizedMessage}",
                    isUser = false
                )
            } finally {
                _isLoading.value = false
                clearFile()
            }
        }
    }

    fun setLoadedBitmap(bitmap: Bitmap?) {
        viewModelScope.launch {
            _loadedBitmap.value = bitmap
        }
    }

    fun clearFile() {
        _loadedBitmap.value = null
    }
}