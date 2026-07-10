package com.example.livecodeimproved.coroutines.retryqueue.main

import com.example.livecodeimproved.core.di.StubConfig
import com.example.livecodeimproved.core.di.stubResult
import com.example.livecodeimproved.coroutines.retryqueue.models.Task
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.incrementAndFetch

/**
 * Требования:
 *
 * есть 20 задач;
 * каждая задача обрабатывается worker-ами;
 * worker-ов — 4;
 * задача может случайно завершиться ошибкой;
 * если ошибка произошла, задачу нужно повторить;
 * максимум попыток — 3;
 * после 3 неудачных попыток задача считается failed;
 * в конце вывести:
 * successful count;
 * failed count;
 * сколько всего retry было сделано.
 */
@OptIn(ExperimentalAtomicApi::class)
fun main(): Unit = runBlocking {

    val tasks = List(20) { i ->
        Task(id = i + 1)
    }

    val total = tasks.size

    val taskChannel = Channel<Task>(20)

    launch {
        tasks.forEach {
            taskChannel.send(it)
        }
    }.join()

    val retryCount = AtomicInt(0)
    val done = AtomicInt(0)
    val failed = AtomicInt(0)
    val success = AtomicInt(0)

    (0..3).map {
        launch {
            for (task in taskChannel) {
                try {
                    if (task.attempt >= 3) {
                        failed.incrementAndFetch()
                        done.incrementAndFetch()
                    } else {
                        val result = handleTask(task)
                        if (result.isFailure) {
                            retryCount.incrementAndFetch()
                            val retryTask = task.copy(attempt = task.attempt + 1)
                            taskChannel.send(retryTask)
                        } else {
                            success.incrementAndFetch()
                            done.incrementAndFetch()
                        }
                    }

                    if(done.load() >= total) {
                        taskChannel.close()
                    }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                    failed.incrementAndFetch()
                    done.incrementAndFetch()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }.joinAll()

    println("Retry $retryCount")
    println("Failed $failed")
    println("Success $success")

}

suspend fun handleTask(task: Task): Result<Task> {
    val config = StubConfig(
        delay = 300,
        message = "Error",
        data = task
    )
    if (task.id % 2 != 0) {
        return Result.failure(Exception("Custom error"))
    }

    return try {
        stubResult(config) {
            Result.success(task)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}