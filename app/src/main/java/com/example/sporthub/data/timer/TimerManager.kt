package com.example.sporthub.data.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object TimerManager { //object + курутина позволяет работать пока живет приложение и есть память под это
    private val _timerLeft = MutableStateFlow(0L)
    val timeLeft: StateFlow<Long> = _timerLeft.asStateFlow()

    private val _totalSeconds= MutableStateFlow(0L)
    val totalSeconds: StateFlow<Long> = _totalSeconds.asStateFlow()

    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun startTimer(duration: Long) {
        timerJob?.cancel()
        _timerLeft.value = duration
        _totalSeconds.value = duration
        runTimer()
    }

    fun resumeTimer() {
        if(timerJob?.isActive == true) return
        runTimer()
    }

    private fun runTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            while(_timerLeft.value > 0) {
                delay(1000L)
                _timerLeft.value -= 1
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timerLeft.value = 0
        _totalSeconds.value = 0
    }
}