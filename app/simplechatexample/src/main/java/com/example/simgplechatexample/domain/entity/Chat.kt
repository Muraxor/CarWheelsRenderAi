package com.example.simgplechatexample.domain.entity

data class Chat(
    val id: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val avatar: String?,
    val unreadCount: Int
)
