package com.example.livecodeimproved.coroutines.eventprocessor

import com.example.livecodeimproved.core.di.StubConfig
import com.example.livecodeimproved.core.di.stubWork
import com.example.livecodeimproved.core.di.tryCatch
import com.example.livecodeimproved.coroutines.eventprocessor.models.Event
import com.example.livecodeimproved.coroutines.eventprocessor.models.Type
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

/**
 * Требования:
 *
 * есть producer, который генерирует 100 событий;
 * события бывают типов "click", "scroll", "purchase";
 * нужно обработать все события;
 * "purchase" должен обрабатываться только одним dedicated worker-ом;
 * остальные события могут обрабатываться пулом из 3 worker-ов;
 * программа должна завершиться корректно;
 * каждое событие должно быть обработано ровно один раз.
 */
class EventProcessorImpl : com.example.livecodeimproved.coroutines.eventprocessor.EventProcessor {

    private val purchaseSemaphore = Semaphore(1)
    private val otherSemaphore = Semaphore(3)

    override suspend fun process(event: Event) = supervisorScope {
        //val purchaseChannel = Channel<Event>(100)

        when (event.type) {
            Type.PURCHASE -> handlePurchaseEvent(event)
            Type.CLICK, Type.SCROLL -> handleOtherEvent(event, 1)
        }

        //println("Handled events $handled")

        Unit
    }

    suspend fun handlePurchaseEvent(event: Event): Boolean {
        require(event.type == Type.PURCHASE)

        return tryCatch({
            stubWork(StubConfig(300, "Purchase event handle error", event))
            true
        }, {
            println("Error when handling $event")
            false
        })!!


    }

    suspend fun handleOtherEvent(event: Event, workerId: Int): Boolean {
        require(event.type != Type.PURCHASE)
        println("Worker $workerId")

        return tryCatch({
            stubWork(StubConfig(300, "Purchase event handle error", event))
            true
        }, {
            println("Error when handling $event")
            false
        })!!
    }
}