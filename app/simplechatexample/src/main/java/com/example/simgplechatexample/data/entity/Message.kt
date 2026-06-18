package com.example.simgplechatexample.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simgplechatexample.domain.entity.Message

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val senderId: String,
    val senderName: String,
    val senderAvatar: String?,
    val timestamp: Long,
    val status: String,  // String, не enum!
    val syncStatus: String
)

// Маппер для преобразования между слоями
fun MessageEntity.toDomain(): Message = Message(
    id = id,
    text = text,
    senderId = senderId,
    senderName = senderName,
    senderAvatar = senderAvatar,
    timestamp = timestamp,
    status = Message.MessageStatus.valueOf(status),
    syncStatus = Message.SyncStatus.valueOf(syncStatus)
)

fun Message.toEntity(): MessageEntity = MessageEntity(
    id = id,
    text = text,
    senderId = senderId,
    senderName = senderName,
    senderAvatar = senderAvatar,
    timestamp = timestamp,
    status = status.name,
    syncStatus = syncStatus.name
)