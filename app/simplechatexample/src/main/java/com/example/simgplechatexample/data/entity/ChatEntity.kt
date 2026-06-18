package com.example.simgplechatexample.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simgplechatexample.domain.entity.Chat

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val avatar: String?,
    val unreadCount: Int
)

fun ChatEntity.toDomain(): Chat {
    return Chat(
        id = this.id,
        name = this.name,
        lastMessage = this.lastMessage,
        lastMessageTime = this.lastMessageTime,
        avatar = this.avatar,
        unreadCount = this.unreadCount
    )
}

fun Chat.toEntity(): ChatEntity {
    return ChatEntity(
        id = this.id,
        name = this.name,
        lastMessage = this.lastMessage,
        lastMessageTime = this.lastMessageTime,
        avatar = this.avatar,
        unreadCount = this.unreadCount
    )
}


