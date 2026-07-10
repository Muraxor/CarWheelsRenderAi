package com.example.livecodeimproved.coroutines.eventprocessor.models

data class Event(
    val id: Int,
    val type: com.example.livecodeimproved.coroutines.eventprocessor.models.Type
)

enum class Type {
    PURCHASE, SCROLL,CLICK
}
