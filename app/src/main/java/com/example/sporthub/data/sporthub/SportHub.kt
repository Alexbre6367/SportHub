package com.example.sporthub.data.sporthub

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Keep
@Entity(tableName = "user_table", primaryKeys = ["userId", "date"])
data class User(
    val userId: String,
    val date: Long,
    val level: Int = 0,
    val name: String = "",
    val gender: String = "",
    val weight: Int = 0,
    val height: Int = 0,
    val birthdate: Long = 0L,
    val version: Int = 0,
    val uri: String? = "",
    val strike: Int = 0,
    val select: Boolean = false
)

@Entity(tableName = "health_table")
data class HealthEntity(
    @PrimaryKey val dateId: Long,
    val steps: Long,
    val heart: Int?,
    val sleep: Long?,
    val oxygen: Int?,
    val water: Int?,
    val calories: Int?
)

@Entity(tableName = "workout_table")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val workoutId: Int = 0,
    val name: String = ""
)

@Entity(
    tableName = "exercise_table",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["workoutId"],
            childColumns = ["workoutOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val exerciseId: Int = 0,
    val workoutOwnerId: Int,
    val body: String = "",
    val repetitions: Int = 0,
    val sets: Int = 0,
    val kg: String = ""
)

data class WorkoutWithExercises(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutOwnerId"
    ) val exercises: List<ExerciseEntity>
)

