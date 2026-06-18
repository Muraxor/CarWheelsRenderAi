package com.example.simgplechatexample.presentation.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simgplechatexample.domain.entity.Chat
import com.example.simgplechatexample.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            _isLoading.value = true

            // Синхронизация чатов
            try {
                repository.syncChatsFromNetwork()
            } catch (e: Exception) {
                // Игнорируем, используем кэш
            }

            // Подписка на поток чатов
            repository.getChatsStream().collect { chatList ->
                _chats.value = chatList
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            repository.syncChatsFromNetwork()
        }
    }
}
