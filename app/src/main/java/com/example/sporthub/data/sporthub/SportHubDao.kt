package com.example.sporthub.data.sporthub

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SportHubDao{
    @Query("SELECT * FROM user_table WHERE userId = :userId")
    suspend fun getUser(userId: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
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