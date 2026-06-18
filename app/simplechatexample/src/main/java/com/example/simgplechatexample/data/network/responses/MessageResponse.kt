package com.example.simgplechatexample.data.network.responses

data class MessageResponse(
    val id: String,
    val text: String,
    val senderId: String,
    val senderName: String,
    val senderAvatar: String?,
    val createdAt: Long,  // timestamp в миллисекундах
    val replyToId: String? = null
)

data class PaginatedResponse<T>(
    val data: List<T>,
    val pagination: Pagination
)

data class Pagination(
    val currPage: Int,
    val nextPage: Int?,
    val prevPage: Int?,
    val pageSize: Int,
    val totalPages: Int,
    val totalRecords: Int
)