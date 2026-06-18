package com.example.simgplechatexample.presentation.chatlist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simgplechatexample.domain.entity.Message
import com.example.simgplechatexample.domain.repository.ChatRepository
import com.example.simgplechatexample.domain.repository.MessagePage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var currentPage = 1

    init {
        loadInitialMessages()
    }

    private fun loadInitialMessages() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                repository.syncMessagesFromNetwork()

                repository.getMessagesStream().collect { page ->
                    _messages.update {
                        page.data
                    }
                    currentPage = page.nextKey?.minus(1) ?: 1
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun loadMore() {
//        viewModelScope.launch {
//            val nextPage = currentPage + 1
//            val page = repository.loadNextPage(nextPage)
//
//            if (page.data.isNotEmpty()) {
//                _messages.update {
//                    it + page.data
//                }
//                currentPage = nextPage
//            }
//        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _isSending.value = true

            val message = Message(
                id = System.currentTimeMillis().toString(),
                text = text,
                senderId = "current_user",
                senderName = "Me",
                senderAvatar = null,
                timestamp = System.currentTimeMillis(),
                status = Message.MessageStatus.SENT,
                syncStatus = Message.SyncStatus.PENDING
            )

            try {
                repository.sendMessage(message)
                _isSending.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isSending.value = false
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                repository.syncMessagesFromNetwork()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}