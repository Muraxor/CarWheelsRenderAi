package com.example.simgplechatexample.data.network.request

data class SendMessageRequest(
    val text: String,
    val senderId: String,
    val senderName: String,
    val senderAvatar: String?,
    val createdAt: Long = System.currentTimeMillis()
)