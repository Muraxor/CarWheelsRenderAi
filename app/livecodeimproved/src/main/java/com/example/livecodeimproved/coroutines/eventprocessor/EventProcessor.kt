package com.example.livecodeimproved.coroutines.eventprocessor

import com.example.livecodeimproved.coroutines.eventprocessor.models.Event


interface EventProcessor {


    suspend fun process(event: com.example.livecodeimproved.coroutines.eventprocessor.models.Event)
}