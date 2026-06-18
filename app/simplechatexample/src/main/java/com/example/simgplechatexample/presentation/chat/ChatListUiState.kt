package com.example.simgplechatexample.presentation.chat

import com.example.simgplechatexample.domain.entity.Message

sealed interface ChatUiState {

    // Состояние загрузки данных
    data class Loading(
        val isFirstLoad: Boolean = true
    ) : ChatUiState

    // Основное состояние (успешная загрузка)
    data class Success(
        val messages: List<Message>,
        val paginationState: PaginationState,
        val sendMessageState: SendMessageState,
        val refreshState: RefreshState = RefreshState.Idle
    ) : ChatUiState

    // Состояние ошибки
    data class Error(
        val error: ChatError,
        val cachedMessages: List<Message> = emptyList()
    ) : ChatUiState
}

// Специализированные состояния
data class PaginationState(
    val hasMore: Boolean = true,
    val isLoadingMore: Boolean = false,
    val currentPage: Int = 1
)

sealed interface SendMessageState {
    object Idle : SendMessageState
    object Sending : SendMessageState
    data class Error(val message: String) : SendMessageState
    object Success : SendMessageState
}

sealed interface RefreshState {
    object Idle : RefreshState
    object Loading : RefreshState
    data class Error(val message: String) : RefreshState
}

sealed interface ChatError {
    object NetworkError : ChatError
    data class SyncError(val message: String) : ChatError
    data class Unknown(val message: String) : ChatError
}
