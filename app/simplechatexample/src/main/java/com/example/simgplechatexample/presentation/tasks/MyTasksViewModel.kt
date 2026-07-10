package com.example.simgplechatexample.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTasksViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<MyTasksUiState>(Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            val tasks = getTasks()
            val total = tasks.size
            val completed = tasks.count { it.completed }
            val progress = completed * 100 / total
            _state.update {
                Content(
                    2, 7,
                    tasks,
                    Progress(value = progress.div(100f), text = "$completed/$total")
                )
            }
        }
    }

    fun getTasks() = listOf(
        Task("Купить продукты", completed = false, checked = false),
        Task("Позвонить маме", completed = true, checked = true),
        Task("Почитать книгу", completed = true, checked = false)
    )
}