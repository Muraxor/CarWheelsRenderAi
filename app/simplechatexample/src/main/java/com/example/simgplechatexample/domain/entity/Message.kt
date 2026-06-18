package com.example.simgplechatexample.domain.entity

data class Message(
    val id: String,
    val text: String,
    val senderId: String,
    val senderName: String,
    val senderAvatar: String?,
    val timestamp: Long,
    val status: MessageStatus,
    val syncStatus: SyncStatus
) {
    enum class MessageStatus { SENT, DELIVERED, READ }
    enum class SyncStatus { SYNCED, PENDING, FAILED }
}
