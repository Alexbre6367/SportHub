package com.example.sporthub.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sporthub.data.repository.SportHubRepository
import com.example.sporthub.data.sporthub.ExerciseEntity
import com.example.sporthub.data.sporthub.SportHubDatabase
import com.example.sporthub.data.sporthub.WorkoutEntity
import com.example.sporthub.data.sporthub.WorkoutWithExercises
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SportHubDatabase.getInstance(application)
    private val repository = SportHubRepository(db.sportHubDao, db.healthDao, db.workoutDao)

    val workoutList: LiveData<List<WorkoutWithExercises>> = repository.allWorkout.asLiveData()
    
    private val _selectedExercises = MutableStateFlow<Set<Int>>(emptySet())
    val selectedExercises = _selectedExercises.asStateFlow()

    fun addWorkoutWithExercise(workout: WorkoutEntity, exercises: List<ExerciseEntity>) {
        viewModelScope.launch {
            repository.addWorkoutWithExercises(workout, exercises)
        }
    }

    fun deleteWorkoutWithExercise(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.deleteWorkoutWithExercises(workout)
        }
    }
    
    fun toggleExercises(id: Int) {
        _selectedExercises.value = if(id in _selectedExercises.value) {
            selectedExercises.value - id
        } else {
            selectedExercises.value + id
        }
    }
    
    fun deleteSelectedExercises() {
        viewModelScope.launch {
            _selectedExercises.value.forEach { id ->
                repository.deleteExercise(id)
            }
            _selectedExercises.value = emptySet()
        }
    }

    fun clearSelectedExercises() {
        _selectedExercises.value = emptySet()
    }

}