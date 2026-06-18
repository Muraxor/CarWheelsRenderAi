package com.example.simgplechatexample.data.db.mappers

import com.example.simgplechatexample.data.entity.MessageEntity
import com.example.simgplechatexample.data.network.responses.MessageResponse
import com.example.simgplechatexample.domain.entity.Message

object MessageMapper {

    // Преобразование из API Response в Entity для Room
    fun toEntity(response: MessageResponse): MessageEntity {
        return MessageEntity(
            id = response.id,
            text = response.text,
            senderId = response.senderId,      // ← маппим опечатку API
            senderName = response.senderName,
            senderAvatar = response.senderAvatar,
            timestamp = response.createdAt,    // ← маппим опечатку API
            status = "SENT",                    // значение по умолчанию
            syncStatus = "SYNCED"               // значение по умолчанию
        )
    }

    // Преобразование из Domain в Entity
    fun toEntity(domain: Message): MessageEntity {
        return MessageEntity(
            id = domain.id,
            text = domain.text,
            senderId = domain.senderId,
            senderName = domain.senderName,
            senderAvatar = domain.senderAvatar,
            timestamp = domain.timestamp,
            status = domain.status.name,
            syncStatus = domain.syncStatus.name
        )
    }

    // Преобразование из Entity в Domain
    fun toDomain(entity: MessageEntity): Message {
        return Message(
            id = entity.id,
            text = entity.text,
            senderId = entity.senderId,
            senderName = entity.senderName,
            senderAvatar = entity.senderAvatar,
            timestamp = entity.timestamp,
            status = Message.MessageStatus.valueOf(entity.status),
            syncStatus = Message.SyncStatus.valueOf(entity.syncStatus)
        )
    }
}