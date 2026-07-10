package com.example.livecodeimproved.coroutines.retryqueue.models

data class Task(
    val id: Int,
    val attempt: Int = 1
)
