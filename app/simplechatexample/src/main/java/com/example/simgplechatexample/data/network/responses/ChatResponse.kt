package com.example.simgplechatexample.data.network.responses

import com.example.simgplechatexample.data.entity.ChatEntity

data class ChatResponse(
    val id: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val avatar: String?,
    val unreadCount: Int
)

fun ChatResponse.toEntity(): ChatEntity {
    return ChatEntity(
        id = this.id,
        name = this.name,
        lastMessage = this.lastMessage,
        lastMessageTime = this.lastMessageTime,
        avatar = this.avatar,
        unreadCount = this.unreadCount
    )
}
