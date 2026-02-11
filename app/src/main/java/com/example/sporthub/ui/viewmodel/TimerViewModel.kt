package com.example.sporthub.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.sporthub.data.timer.TimerManager
import com.example.sporthub.data.timer.TimerService
import com.example.sporthub.data.timer.TimerService.Companion.ACTION_STOP_SERVICE

class TimerViewModel: ViewModel() {
    val timerLeft = TimerManager.timeLeft
    val totalSeconds = TimerManager.totalSeconds

    var isPaused by mutableStateOf(false)

    fun startTimer(context: Context, duration: Long) {
        isPaused = false
        TimerManager.startTimer(duration)

        val intent = Intent(context, TimerService::class.java)
        context.startForegroundService(intent)

    }

    fun resumeTimer() {
        isPaused = false
        TimerManager.resumeTimer()
    }


    fun stopTimer() {
        isPaused = true
        TimerManager.stopTimer()
    }

    fun resetTimer(context: Context) {
        isPaused = false
        TimerManager.resetTimer()

        val intent = Intent(context, TimerService::class.java).apply {
            action = ACTION_STOP_SERVICE
        }
        context.startService(intent)
    }

    fun formatTime(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%02d:%02d".format(minutes, remainingSeconds)
    }
}