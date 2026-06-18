package com.example.simgplechatexample.data.network

import com.example.simgplechatexample.data.network.request.SendMessageRequest
import com.example.simgplechatexample.data.network.responses.MessageResponse
import com.example.simgplechatexample.data.network.responses.PaginatedResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// data/network/ChatApi.kt
interface ChatApi {

    // ✅ Добавляем метод для загрузки всех сообщений
    @GET("api/v1/chatList/Chats")
    suspend fun getAllMessages(): List<MessageResponse>

    // ✅ Метод для отправки сообщения
    @POST("api/v1/chatList/Chats")
    suspend fun sendMessage(
        @Body message: SendMessageRequest
    ): MessageResponse

    @GET("api/v1/chat/messages")
    suspend fun getMessages(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("order") order: String = "desc"
    ): PaginatedResponse<MessageResponse>
}