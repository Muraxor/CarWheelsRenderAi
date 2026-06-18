package com.example.simgplechatexample.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simgplechatexample.data.db.dao.ChatDao
import com.example.simgplechatexample.data.db.dao.MessageDao
import com.example.simgplechatexample.data.entity.ChatEntity
import com.example.simgplechatexample.data.entity.MessageEntity

@Database(
    entities = [MessageEntity::class, ChatEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: ChatDatabase? = null

        fun init(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ChatDatabase::class.java,
                    "chat_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build().also { INSTANCE = it }
            }
        }

        fun getInstance(): ChatDatabase {
            return INSTANCE ?: error("Call init() first in Application")
        }
    }
}
