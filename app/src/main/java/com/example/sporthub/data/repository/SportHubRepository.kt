package com.example.sporthub.data.repository

import android.icu.util.Calendar
import android.util.Log
import androidx.health.connect.client.units.Energy
import com.example.sporthub.data.sporthub.ExerciseEntity
import com.example.sporthub.data.sporthub.FaceDao
import com.example.sporthub.data.sporthub.FaceEntity
import com.example.sporthub.data.sporthub.HealthDao
import com.example.sporthub.data.sporthub.HealthEntity
import com.example.sporthub.data.sporthub.SportHubDao
import com.example.sporthub.data.sporthub.User
import com.example.sporthub.data.sporthub.WorkoutDao
import com.example.sporthub.data.sporthub.WorkoutEntity
import com.example.sporthub.data.sporthub.WorkoutWithExercises
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class SportHubRepository(
    private val sportHubDao: SportHubDao,
    private val healthDao: HealthDao,
    private val workoutDao: WorkoutDao,
    private val faceDao: FaceDao
) {

    suspend fun addUser(user: User) {
        sportHubDao.addUser(user)
    }

    suspend fun saveUserDaily(user: User) {
        val cal = Calendar.getInstance()
        val today = (cal.get(java.util.Calendar.YEAR) * 10000 +
                (cal.get(java.util.Calendar.MONTH) + 1) * 100 +
                cal.get(java.util.Calendar.DAY_OF_MONTH)).toLong()

        sportHubDao.addUser(user.copy(date = today))
    }

    fun getUser(userId: String): Flow<User?> {
        return sportHubDao.getUser(userId)
    }

    suspend fun updateUser(user: User) {
        sportHubDao.updateUser(user)
    }

    suspend fun deleteUser(userId: String) {
        sportHubDao.deleteUser(userId)
        try {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .delete()
                .await()
            Log.d("MyLog", "Пользователь успешно удален из Firestore")
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка при удалении пользователя", e)
        }
    }

    fun getUserForWeek(startWeek: Long, endWeek: Long): Flow<List<User>> {
        return sportHubDao.getUserWeek(startWeek, endWeek)
    }

    suspend fun saveHealth(steps: Long, sleep: Long, heart: Int, oxygen: Int, water: Int, calories: Energy) {
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
                calories = calories.inKilocalories.toInt()
            )
        )
    }
    fun getHealthForToday(dateId: Long): Flow<HealthEntity?> {
        return healthDao.getLastHealthData(dateId)
    }

    fun getHealthForWeek(startWeek: Long, endWeek: Long): Flow<List<HealthEntity>> {
        return healthDao.getHealthWeek(startWeek, endWeek)
    }

    //тренировки ниже

    val allWorkout: Flow<List<WorkoutWithExercises>> = workoutDao.getAllWorkout()

    suspend fun addWorkoutWithExercises(workout: WorkoutEntity, exercises: List<ExerciseEntity>) {
        val workoutId = workoutDao.addWorkout(workout)
        val exerciseId = exercises.map {
            if(it.exerciseId < 0) {
                it.copy(exerciseId = 0, workoutOwnerId = workoutId.toInt())
            } else {
                it.copy(workoutOwnerId = workoutId.toInt())
            }
        }
        workoutDao.addExercise(exerciseId)
        Log.d("MyLog", "Тренировка добавлена $workoutId")
    }

    suspend fun deleteWorkoutWithExercises(workout: WorkoutEntity) {
        workoutDao.deleteWorkout(workout)
    }

    suspend fun deleteExercise(id: Int) {
        workoutDao.deleteExercise(id)
    }


    suspend fun addFace(sensitive: Float, acne: Int, dryness: Int, moisture: Int) {
        faceDao.addSensitive(FaceEntity(1, sensitive, acne, dryness, moisture))
    }

    val getFaceData: Flow<FaceEntity?> = faceDao.getFace()

    suspend fun updateWidget(widget: Boolean) {
        faceDao.updateWidget(widget)
    }
}