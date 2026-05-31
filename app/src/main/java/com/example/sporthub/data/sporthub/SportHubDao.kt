package com.example.sporthub.data.sporthub

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SportHubDao{
    @Query("SELECT * FROM user_table WHERE userId = :userId ORDER BY date DESC LIMIT 1 ")
    fun getUser(userId: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM user_table WHERE userId = :userId")
    suspend fun deleteUser(userId: String): Int

    @Query("SELECT * FROM user_table WHERE date >= :startWeek AND date <= :endWeek")
    fun getUserWeek(startWeek: Long, endWeek: Long): Flow<List<User>>
}

@Dao
interface HealthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthData(healthData: HealthEntity)

    @Query("SELECT * FROM health_table WHERE dateId = :dateId")
    fun getLastHealthData(dateId: Long): Flow<HealthEntity?>

    @Query("SELECT * FROM health_table WHERE dateId >= :startWeek AND dateId <= :endWeek")
    fun getHealthWeek(startWeek: Long, endWeek: Long): Flow<List<HealthEntity>>

    @Query("DELETE FROM health_table")
    suspend fun deleteAllHealthData()
}

@Dao
interface WorkoutDao {
    @Transaction
    @Query("Select * FROM workout_table")
    fun getAllWorkout(): Flow<List<WorkoutWithExercises>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWorkout(workout: WorkoutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExercise(exercises: List<ExerciseEntity>)

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

    @Query("DELETE FROM exercise_table WHERE exerciseId = :id")
    suspend fun deleteExercise(id: Int)
}

@Dao
interface FaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSensitive(sensitive: FaceEntity): Long

    @Query("Select * FROM face_table WHERE id = 1")
    fun getFace(): Flow<FaceEntity?>

    @Query("UPDATE face_table SET widget = :widget WHERE id = 1")
    suspend fun updateWidget(widget: Boolean)
}