package com.example.livecodeimproved.coroutines.debouncedlogger.main

import com.example.livecodeimproved.coroutines.debouncedlogger.models.LogEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.random.Random

/**
 * Требования:
 * несколько producer-ов быстро отправляют события;
 * logger должен печатать события пачками; пачка печатается,
 * если: накопилось 10 событий;
 * или прошло 500 мс с момента первого события в текущей пачке;
 * после завершения producer-ов оставшиеся события тоже должны быть напечатаны;
 * программа не должна зависнуть.
 */
@OptIn(DelicateCoroutinesApi::class, ExperimentalAtomicApi::class)
fun main(): Unit = runBlocking {
    val channel = Channel<LogEvent>(UNLIMITED)

    val producers = mutableListOf<Job>()
    val total = AtomicInt(0)

    repeat(3) {
        launch {
            repeat(40) {
                val delay = Random.nextLong(100, 300)
                delay(delay)
                val event =
                    LogEvent(
                        "Hi",
                        timestamp = System.currentTimeMillis()
                    )
                channel.send(event)
            }
        }.also {
            producers.add(it)
        }
    }

    val worker = launch {
        val events = mutableListOf<LogEvent>()
        for (event in channel) {
            events.add(event)

            val diffTime = System.currentTimeMillis() - events.first().timestamp

            if (events.size == 10) {
                total.addAndFetch(events.size)
                println("${events.size} $events")
                events.clear()
            } else {
                if (diffTime >= 500) {
                    total.addAndFetch(events.size)
                    println("${events.size} $events")
                    events.clear()
                }
            }
        }

        if (events.isNotEmpty()) {
            total.addAndFetch(events.size)
            println("${events.size} $events")
        }
    }

    producers.joinAll()

    channel.close()

    worker.join()

    println("End $total")
}