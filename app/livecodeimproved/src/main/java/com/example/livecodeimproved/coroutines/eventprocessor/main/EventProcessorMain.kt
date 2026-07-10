package com.example.livecodeimproved.coroutines.eventprocessor.main

import com.example.livecodeimproved.coroutines.eventprocessor.EventProcessorImpl
import com.example.livecodeimproved.coroutines.eventprocessor.models.Event
import com.example.livecodeimproved.coroutines.eventprocessor.models.Type
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun main(): Unit = runBlocking {
    two()
}

fun two(): Unit = runBlocking {
    val inputChannel = Channel<Event>()
    val purchaseChannel = Channel<Event>()
    val otherChannel = Channel<Event>()

    val processor =
        EventProcessorImpl()

    val producer = launch {
        repeat(100) { id ->
            delay(Random.nextLong(10, 100))

            inputChannel.send(
                Event(
                    id = id + 1,
                    type = Type.entries[Random.nextInt(
                        Type.entries.size
                    )]
                )
            )
        }

        inputChannel.close()
    }

    val router = launch {
        for (event in inputChannel) {
            when (event.type) {
                Type.PURCHASE -> purchaseChannel.send(event)
                Type.CLICK, Type.SCROLL -> otherChannel.send(event)
            }
        }

        purchaseChannel.close()
        otherChannel.close()
    }

    val purchaseWorker = launch {
        for (event in purchaseChannel) {
            processor.handlePurchaseEvent(event)
        }
    }

    val otherWorkers = List(3) { workerIndex ->
        launch {
            val workerId = workerIndex + 1

            for (event in otherChannel) {
                processor.handleOtherEvent(event, workerId)
            }
        }
    }

    producer.join()
    router.join()
    purchaseWorker.join()
    otherWorkers.joinAll()
}

fun one() = runBlocking {
    val channel = Channel<Event>(50)

    val generateJob = launch {
        (0..50).forEach { _ ->
            delay(Random.nextLong(10, 100))
            val event =
                Event(
                    id = Random.nextInt(),
                    type = Type.entries[Random.nextInt(3)]
                )
            channel.send(event)
        }
    }

    delay(5000)

    val processor =
        EventProcessorImpl()

    (0..2).forEach { _ ->
        launch {
            for (event in channel) {
                processor.process(event)
            }
        }
    }


    generateJob.join()
    channel.close()
}