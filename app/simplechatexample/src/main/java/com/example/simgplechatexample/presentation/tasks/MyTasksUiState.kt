package com.example.simgplechatexample.presentation.tasks

sealed interface MyTasksUiState

data object Loading: MyTasksUiState

data class Error(
    val message: String
): MyTasksUiState

data class Content(
    val completed: Int,
    val total: Int,
    val tasks: List<Task>,
    val progress: Progress
): MyTasksUiState