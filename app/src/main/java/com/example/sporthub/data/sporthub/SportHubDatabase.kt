package com.example.sporthub.data.sporthub

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        User::class,
        HealthEntity::class,
        WorkoutEntity::class,
        ExerciseEntity::class
    ],
    version = 24,
    exportSchema = false
)
abstract class SportHubDatabase: RoomDatabase() {

    abstract val sportHubDao: SportHubDao
    abstract val healthDao: HealthDao
    abstract val workoutDao: WorkoutDao

    companion object {
        private var INSTANCE: SportHubDatabase? = null

        fun getInstance(context: Context): SportHubDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SportHubDatabase::class.java,
                    "sporthub_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}