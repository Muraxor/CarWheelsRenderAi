package com.example.livecodeimproved.core.di

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlin.random.Random

suspend fun <T> stubWork(config: StubConfig<T>) {
    println("Start work with ${config.data}")

    val randomError = Random.nextInt(2)

    when (randomError) {
        0 -> {
            delay(config.delay)
            println("Complete work with ${config.data}")
        }
        1 -> {
            println("Error work with ${config.data}")
            throw RuntimeException(config.message)
        }
    }
}

suspend inline fun <reified T, E> stubResult(config: StubConfig<E>, factory: () -> T): T {
    stubWork(config)
    return factory.invoke()
}

suspend inline fun <T> tryCatch(
    lambda: suspend () -> T?,
    onError: suspend (Throwable) -> T?
): T? {
    return try {
        lambda.invoke()
    } catch (e: Throwable) {
        if (e is CancellationException) throw e
        onError.invoke(e)
    }
}

suspend fun <T> tryCatchIgnoreError(lambda: suspend () -> T?): T? {
    return tryCatch(
        { lambda() }, {
            null
        }
    )
}

data class StubConfig<T>(
    val delay: Long,
    val message: String?,
    val data: T
)
