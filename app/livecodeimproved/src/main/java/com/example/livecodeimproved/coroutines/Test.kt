package com.example.livecodeimproved.coroutines

import androidx.compose.ui.input.key.Key.Companion.D
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.IOException
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.update
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.io.path.Path
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class, ExperimentalAtomicApi::class)

fun main(): Unit = runBlocking {
    val channel = MyChannel<Int>()

    channel.send(2)

    println(channel.receive())

    launch {
        val value = channel.receive()
        println(value)
    }

    launch {
        val value = channel.receive()
        println(value)
    }

    channel.send(3)

    delay(300)
    channel.send(10)
}


class MyChannel<T> {

    val queue: ArrayDeque<T> = ArrayDeque()

    val waitingReceivers = ArrayList<Continuation<T>>()

    fun send(value: T) {
        val receiver = waitingReceivers.removeFirstOrNull()

        if (receiver != null) {
            receiver.resume(value)
        } else {
            queue.addLast(value)
        }
    }

    suspend fun receive(): T {
        if (queue.isNotEmpty()) {
            return queue.removeFirst()
        }

        return suspendCancellableCoroutine { continuation ->
            waitingReceivers.add(continuation)

            continuation.invokeOnCancellation {
                waitingReceivers.remove(continuation)
            }
        }
    }
}

