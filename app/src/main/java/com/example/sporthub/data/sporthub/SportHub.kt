package com.example.sporthub.data.sporthub

import androidx.annotation.Keep
import androidx.health.connect.client.units.Energy
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val userId: String,
    val level: Int = 0,
    val name: String = "",
    val gender: String = "",
    val weight: Float = 0f,
    val height: Int = 0,
    val birthdate: Long = 0L,
    val version: Int = 0,
    val uri: String? = "",
    val strike: Int = 0,
)

@Entity(tableName = "health_table")
data class HealthEntity(
    @PrimaryKey val dateId: Long,
    val steps: Long,
    val heart: Int?,
    val sleep: Long?,
    val oxygen: Int?,
    val water: Int?,
    val calories: Int?,
    val timestamp: Long = System.currentTimeMillis()
)