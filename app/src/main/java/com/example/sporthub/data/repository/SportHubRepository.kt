package com.example.sporthub.data.repository

import android.content.SharedPreferences
import android.icu.util.Calendar
import androidx.core.content.edit
import androidx.health.connect.client.units.Energy
import com.example.sporthub.data.sporthub.HealthDao
import com.example.sporthub.data.sporthub.HealthEntity
import com.example.sporthub.data.sporthub.SportHubDao
import com.example.sporthub.data.sporthub.User
import kotlinx.coroutines.flow.Flow

class SportHubRepository(private val sportHubDao: SportHubDao, private val healthDao: HealthDao) {

    suspend fun addUser(user: User) {
        sportHubDao.addUser(user)
    }

    suspend fun getUser(userId: String): User? {
        return sportHubDao.getUser(userId)
    }

    suspend fun updateUser(user: User) {
        sportHubDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        sportHubDao.deleteUser(user)
    }

    suspend fun saveHealth(steps: Long, sleep: Long, heart: Int, oxygen: Int, water: Int, calories: Energy, caloriesStrike: Int) {
        val calendar = Calendar.getInstance()
        val dateId = (calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)).toLong()
        healthDao.insertHealthData(
            HealthEntity(
                dateId = dateId,
                steps = steps,
                heart = heart,
                sleep = sleep,
                oxygen = oxygen,
                water = water,
                calories = calories.inKilocalories.toInt(),
                caloriesStrike = caloriesStrike
            )
        )
    }

    suspend fun deleteAllHealthData(preference: SharedPreferences) {
        val calendar = Calendar.getInstance()
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            val todayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
            val lastUpdateDay = preference.getInt("lastUpdateDay", -1)

            if(lastUpdateDay != todayOfYear) {
                healthDao.deleteAllHealthData()
                preference.edit { putInt("lastUpdateDay", todayOfYear) }
            }
        }
    }

    fun getHealthForToday(dateId: Long): Flow<HealthEntity?> {
        return healthDao.getLastHealthData(dateId)
    }

    fun getHealthForWeek(startWeek: Long, endWeek: Long): Flow<List<HealthEntity>> {
        return healthDao.getHealthWeek(startWeek, endWeek)
    }
}