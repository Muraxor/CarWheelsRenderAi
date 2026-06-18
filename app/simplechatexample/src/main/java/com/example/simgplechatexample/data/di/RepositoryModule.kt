package com.example.simgplechatexample.data.di

import com.example.simgplechatexample.data.db.ChatDatabase
import com.example.simgplechatexample.data.db.dao.ChatDao
import com.example.simgplechatexample.data.db.dao.MessageDao
import com.example.simgplechatexample.data.network.ChatApi
import com.example.simgplechatexample.data.network.RetrofitClient
import com.example.simgplechatexample.data.repository.ChatRepositoryImpl
import com.example.simgplechatexample.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideChatApi(): ChatApi = RetrofitClient.chatApi

    @Provides
    @Singleton
    fun provideMessageDao(database: ChatDatabase): MessageDao = database.messageDao()

    @Provides
    @Singleton
    fun provideChatDao(database: ChatDatabase): ChatDao = database.chatDao()

    @Provides
    @Singleton
    fun provideChatRepository(
        dao: MessageDao,
        chatDao: ChatDao,
        api: ChatApi
    ): ChatRepository = ChatRepositoryImpl(dao, chatDao, api)
}