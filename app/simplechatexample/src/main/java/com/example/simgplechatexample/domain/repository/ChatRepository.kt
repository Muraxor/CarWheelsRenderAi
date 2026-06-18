package com.example.simgplechatexample.domain.repository

import androidx.paging.PagingData
import com.example.simgplechatexample.domain.entity.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    /**
     * Поток сообщений с пагинацией
     * Возвращает страницу сообщений с информацией о следующей/предыдущей странице
     */
    fun getMessagesStream(): Flow<MessagePage>

    /**
     * Загрузить следующую страницу
     * @param currentPage Текущая страница
     * @return Новая страница сообщений
     */
    suspend fun loadNextPage(currentPage: Int): MessagePage

    /**
     * Синхронизация с сервером (загрузка всех сообщений)
     */
    suspend fun syncMessagesFromNetwork()

    /**
     * Отправка нового сообщения
     */
    suspend fun sendMessage(message: Message)

    /**
     * Проверка наличия кэшированных сообщений
     */
    suspend fun hasCachedMessages(): Boolean

    /**
     * Очистка кэша
     */
    suspend fun clearCache()
}

data class MessagePage(
    val data: List<Message>,
    val nextKey: Int?,
    val prevKey: Int?,
    val hasMore: Boolean
)