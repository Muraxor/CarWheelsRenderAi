package com.example.simgplechatexample.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.simgplechatexample.data.db.dao.MessageDao
import com.example.simgplechatexample.data.db.mappers.MessageMapper
import com.example.simgplechatexample.data.entity.MessageEntity
import com.example.simgplechatexample.data.network.ChatApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@OptIn(ExperimentalPagingApi::class)
class ChatRemoteMediator(
    private val api: ChatApi,
    private val dao: MessageDao
) : RemoteMediator<Int, MessageEntity>() {

    private val mutex = Mutex()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult = mutex.withLock {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) 1
                    else (getPageNumberByTimestamp(lastItem.timestamp) ?: 1) + 1
                }
            }

            val response = api.getMessages(page = page, limit = PAGE_SIZE)

            // Маппинг из network response в entity
            val entities = response.data.map { MessageMapper.toEntity(it) }
            dao.insertAll(entities)

            MediatorResult.Success(
                endOfPaginationReached = response.pagination.nextPage == null
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getPageNumberByTimestamp(timestamp: Long): Int? {
        // TODO: реализовать через отдельный запрос или хранить page_number в БД
        return null
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}