package com.example.simgplechatexample.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simgplechatexample.data.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    // ============ Пагинация ============

//    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
//    fun getMessagesPagingSource(): PagingSource<Int, MessageEntity>

    // ============ Получение данных ============

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    suspend fun getAllMessages(): List<MessageEntity>

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun observeAllMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun getMessageById(id: String): MessageEntity?

    @Query("SELECT * FROM messages WHERE syncStatus = :status ORDER BY timestamp ASC")
    suspend fun getMessagesBySyncStatus(status: String): List<MessageEntity>

    // ============ Вставка ============

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageEntity>)

    // ============ Обновление ============

    @Query("UPDATE messages SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)

    @Query("UPDATE messages SET status = :status WHERE id = :id")
    suspend fun updateMessageStatus(id: String, status: String)

    // ============ Удаление ============

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM messages")
    suspend fun deleteAll()

    @Query("DELETE FROM messages WHERE syncStatus = :status")
    suspend fun deleteBySyncStatus(status: String)

    // ============ Вспомогательные ============

    @Query("SELECT COUNT(*) FROM messages")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM messages WHERE syncStatus = :status")
    suspend fun getCountBySyncStatus(status: String): Int
}