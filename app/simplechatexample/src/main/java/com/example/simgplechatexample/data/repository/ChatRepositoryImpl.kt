package com.example.simgplechatexample.data.repository

import com.example.simgplechatexample.data.db.dao.ChatDao
import com.example.simgplechatexample.data.db.dao.MessageDao
import com.example.simgplechatexample.data.db.mappers.MessageMapper
import com.example.simgplechatexample.data.entity.toDomain
import com.example.simgplechatexample.data.entity.toEntity
import com.example.simgplechatexample.data.network.ChatApi
import com.example.simgplechatexample.data.network.request.SendMessageRequest
import com.example.simgplechatexample.data.network.responses.toEntity
import com.example.simgplechatexample.domain.entity.Chat
import com.example.simgplechatexample.domain.entity.Message
import com.example.simgplechatexample.domain.repository.ChatRepository
import com.example.simgplechatexample.domain.repository.MessagePage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

//todo убрать состояние из репозитория
@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val dao: MessageDao,
    private val chatDao: ChatDao,
    private val api: ChatApi
) : ChatRepository {

    private val mutex = Mutex()
    private var allMessages = mutableListOf<Message>()

    override fun getMessagesStream(): Flow<MessagePage> = flow {
        val entities = dao.getAllMessages()  // List<MessageEntity>
        val messages = entities.map { it.toDomain() }

        // ✅ Создаём новый список, а не передаём ссылку
        val messagePage = MessagePage(
            data = messages.toList(),  // принудительная копия
            nextKey = if (messages.size > 20) 2 else null,
            prevKey = null,
            hasMore = messages.size > 20
        )
        emit(messagePage)
    }.flowOn(Dispatchers.IO)

    override suspend fun loadNextPage(currentPage: Int): MessagePage {
        TODO("Not yet implemented")
    }

    private fun getPage(pageKey: Int): MessagePage {
        val pageSize = 20
        val start = (pageKey - 1) * pageSize
        val end = minOf(start + pageSize, allMessages.size)

        return MessagePage(
            data = if (start < allMessages.size) {
                allMessages.subList(start, end)
            } else {
                emptyList()
            },
            nextKey = if (end < allMessages.size) pageKey + 1 else null,
            prevKey = if (pageKey > 1) pageKey - 1 else null,
            hasMore = end < allMessages.size
        )
    }

    private fun getCurrentPage(): Int {
        // Простая эвристика: определяем текущую страницу по размеру данных
        // В реальном приложении лучше хранить текущий ключ в отдельной переменной
        return 1
    }

    override suspend fun syncMessagesFromNetwork() = mutex.withLock {
        try {
            val messages = api.getAllMessages()
            val entities = messages.map { MessageMapper.toEntity(it) }
            dao.deleteAll()
            dao.insertAll(entities)
        } catch (e: Exception) {
            throw ChatException.SyncException("Failed to sync", e)
        }
    }

    override suspend fun sendMessage(message: Message) = mutex.withLock {
        try {
            // Сохраняем в БД со статусом PENDING
            val entity = message.toEntity().copy(
                status = "SENT",
                syncStatus = "PENDING"
            )
            dao.insert(entity)

            // Отправляем на сервер
            val request = SendMessageRequest(
                text = message.text,
                senderId = message.senderId,
                senderName = message.senderName,
                senderAvatar = message.senderAvatar,
                createdAt = message.timestamp
            )
            api.sendMessage(request)

            // Обновляем статус
            dao.updateSyncStatus(message.id, "SYNCED")
        } catch (e: Exception) {
            dao.updateSyncStatus(message.id, "FAILED")
            throw ChatException.SendMessageException("Failed to send", e)
        }
    }

    override suspend fun hasCachedMessages(): Boolean {
        return dao.getCount() > 0
    }

    override suspend fun clearCache() {
        dao.deleteAll()
    }

    override fun getChatsStream(): Flow<List<Chat>> = flow {
        while (true) {
            val entities = chatDao.getAllChats()
            val chats = entities.map { it.toDomain() }
            emit(chats)
            delay(1000)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun syncChatsFromNetwork() {
        try {
            val response = api.getAllChats()
            val entities = response.map { it.toEntity() }
            chatDao.deleteAll()
            chatDao.insertAll(entities)
        } catch (e: Exception) {
            // Если API нет, используем заглушку
            val mockChats = listOf(
                Chat("1", "Alice", "Привет!", System.currentTimeMillis(), null, 3),
                Chat("2", "Bob", "Как дела?", System.currentTimeMillis() - 3600000, null, 1),
                Chat("3", "Team", "Встреча в 15:00", System.currentTimeMillis() - 7200000, null, 0)
            )
            val entities = mockChats.map { it.toEntity() }
            chatDao.deleteAll()
            chatDao.insertAll(entities)
        }
    }

    override suspend fun updateUnreadCount(chatId: String, reset: Boolean) {
        if (reset) {
            chatDao.resetUnreadCount(chatId)
        } else {
            chatDao.incrementUnreadCount(chatId)
        }
    }
}