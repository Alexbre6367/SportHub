package com.example.sporthub.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.core.content.edit
import androidx.health.connect.client.units.Energy
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sporthub.data.health.HealthState
import com.example.sporthub.data.repository.SportHubRepository
import com.example.sporthub.data.sporthub.SportHubDatabase
import com.example.sporthub.util.SecureStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = SportHubDatabase.getInstance(application)
    private val repository = SportHubRepository(db.sportHubDao, db.healthDao, db.workoutDao)
    private val healthState = HealthState(application)

    private val secureStorage = SecureStorage.getInstance(application)
    private val _steps = MutableStateFlow(0L)
    val steps = _steps.asStateFlow()

    private val _sleep = MutableStateFlow(0L)
    val sleep = _sleep.asStateFlow()

    private val _heart = MutableStateFlow(0L)
    val heart = _heart.asStateFlow()

    private val _oxygen = MutableStateFlow(0)
    val oxygen = _oxygen.asStateFlow()

    private val _water = MutableStateFlow(0)
    val water = _water.asStateFlow()

    private val _calories = MutableStateFlow(Energy.kilocalories(0.0))
    val calories = _calories.asStateFlow()

    var firstLaunchAnimationCircle = false
    var firstLaunchAnimationCalories = false

    private val _caloriesStrike = MutableStateFlow(0)
    val caloriesStrike = _caloriesStrike.asStateFlow()

    private val _week = MutableStateFlow(List(7) { false })
    val week = _week.asStateFlow()

    private val _entryWeek = MutableStateFlow(List(7) { false })
    val entryWeek = _entryWeek.asStateFlow()

    @SuppressLint("DefaultLocale")
    val formatSleep = _sleep.map { totalSleep ->
        val hours = totalSleep / 60
        val minutes = totalSleep % 60
        String.format("%02d:%02d", hours, minutes)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "00:00")

    init {
        viewModelScope.launch {
            resetWater()
            strikeDay()
            fetchUserEntries()
        }
        caloriesStrike()

        viewModelScope.launch {
            repository.getHealthForToday(getDayId(Calendar.getInstance())).collect { health ->
                if(health != null) {
                    _steps.value = health.steps
                    _sleep.value = health.sleep ?: 0L
                    _heart.value = health.heart?.toLong() ?: 0L
                    _oxygen.value = health.oxygen ?: 0
                    _water.value = health.water ?: 0
                    _calories.value = Energy.kilocalories(health.calories?.toDouble() ?: 0.0)
                }
            }
        }
    }

    private suspend fun saveCurrentHealth() {
        repository.saveHealth(
            steps = _steps.value,
            sleep = _sleep.value,
            heart = _heart.value.toInt(),
            oxygen = _oxygen.value,
            water = _water.value,
            calories = _calories.value,
        )
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                val uid = secureStorage.getUserId()
                val user = uid?.let { repository.getUser(it) }?.first()
                val weight = user?.weight

                if (healthState.checkPermissions()) {
                    val currentSteps = healthState.readStepsToday()
                    val currentSleep = healthState.readSleepToday()
                    val currentHeart = healthState.readHeartToday()
                    val currentOxygen = healthState.readOxygenToday()
                    val currentCalories = healthState.readCalories(currentSteps, weight)

                    _steps.value = currentSteps
                    _sleep.value = currentSleep
                    _heart.value = currentHeart
                    _oxygen.value = currentOxygen
                    _calories.value = currentCalories

                    saveCurrentHealth()

                    Log.d("MyLog", "Показатели здоровья сохранены в базу")
                    Log.d("MyCaloriesStrike", "Калории получены: ${calories.value}")
                } else {
                    Log.e("MyLog", "Разрешение не предоставлено")
                }
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка при получении данных здоровья", e)
            }
        }
    }

    fun addWater() {
        viewModelScope.launch {
            if (_water.value < 10) {
                _water.value += 1
                saveCurrentHealth()
                Log.d("MyLog", "Вода увеличена: ${_water.value}")
            } else {
                Log.d("MyLog", "Достигнут предел воды 10 стаканов")
            }
        }
    }

    private suspend fun resetWater() {
        val prefs = getApplication<Application>().getSharedPreferences(
            "daily_reset_prefs",
            MODE_PRIVATE
        )
        val lastResetDate = prefs.getLong("last_reset_date", 0)

        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.YEAR) * 10000 +
                (calendar.get(Calendar.MONTH) + 1) * 100 +
                calendar.get(Calendar.DAY_OF_MONTH)).toLong()

        if (lastResetDate < today) {
            _water.value = 0
            prefs.edit { putLong("last_reset_date", today) }
            saveCurrentHealth()
            Log.d("MyLog", "Вода успешно сброшена")
        }
    }


    private fun fetchUserEntries() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val startOfWeek = getDayId(calendar)

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val endOfWeek = getDayId(calendar)

            repository.getUserForWeek(startOfWeek, endOfWeek).collect { entities ->
                val progress = MutableList(7) { false }
                val map = entities.map { it.date }.toSet()
                val tempCalendar = Calendar.getInstance()
                tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

                for (i in 0..6) {
                    progress[i] = map.contains(getDayId(tempCalendar))
                    tempCalendar.add(Calendar.DAY_OF_MONTH, 1)
                }

                _entryWeek.value = progress
            }
        }
    }

    fun strikeDay() {
        viewModelScope.launch {
            val uid = secureStorage.getUserId() ?: return@launch
            val user = repository.getUser(uid).first() ?: return@launch
            val today = getDayId(Calendar.getInstance())

            repository.saveUserDaily(user)

            val prefs = getApplication<Application>().getSharedPreferences(
                "daily_reset_prefs",
                MODE_PRIVATE
            )
            val lastStrikeDate = prefs.getLong("last_strike_date", -1)
            if (lastStrikeDate != today) {
                val updatedUser = user.copy(strike = user.strike + 1)
                repository.saveUserDaily(updatedUser)

                prefs.edit { putLong("last_strike_date", today) }

                Log.d("MyLog", "Strike увеличен до: ${updatedUser.strike}")
            }
        }
    }

    private fun caloriesStrike() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val startOfWeek = getDayId(calendar)

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val endOfWeek = getDayId(calendar)

            repository.getHealthForWeek(startOfWeek, endOfWeek).collect { entities ->
                val progress = MutableList(7) { false }
                val map = entities.associateBy { it.dateId }
                val tempCalendar = Calendar.getInstance()
                tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

                for (i in 0..6) {
                    val currentId = getDayId(tempCalendar)
                    val health = map[currentId]

                    progress[i] = (health?.calories ?: 0) >= 300
                    tempCalendar.add(Calendar.DAY_OF_MONTH, 1)
                }

                _caloriesStrike.value = progress.count { it }
                _week.value = progress
            }
        }
    }

    private fun getDayId(cal: Calendar): Long {
        return (cal.get(Calendar.YEAR) * 10000 +
                (cal.get(Calendar.MONTH) + 1) * 100 +
                cal.get(Calendar.DAY_OF_MONTH)).toLong()
    }
}