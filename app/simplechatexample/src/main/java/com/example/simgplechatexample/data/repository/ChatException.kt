package com.example.simgplechatexample.data.repository


// Исключения
sealed class ChatException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class SyncException(message: String, cause: Throwable? = null) : ChatException(message, cause)
    class SendMessageException(message: String, cause: Throwable? = null) : ChatException(message, cause)
    class NoCacheException(message: String) : ChatException(message)
}