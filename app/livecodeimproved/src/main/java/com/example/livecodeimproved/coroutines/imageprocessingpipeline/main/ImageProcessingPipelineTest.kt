package com.example.livecodeimproved.coroutines.imageprocessingpipeline.main

import com.example.livecodeimproved.coroutines.imageprocessingpipeline.AsyncImageProcessingPipelineImpl
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.channels.ChannelsImageProcessingPipelineImpl
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.Image
import com.example.livecodeimproved.coroutines.limitedparalleldownloader.urls
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.LongAdder
import kotlin.concurrent.thread
import kotlin.random.Random




var b = 0

var a = 0

fun main(): Unit = runBlocking {
//    val asyncWorker =
//        AsyncImageProcessingPipelineImpl()
//    val channelsWorker =
//        ChannelsImageProcessingPipelineImpl()
//
//    channelsWorker.processImages(list).also {
//        println("Response $it")
//    }

    val handler = CoroutineExceptionHandler { _, _ ->
        println("Handled")
    }

    launch(handler) {
        delay(50)
        error("Boom")
    }

    delay(100)
    println("Done")
}

class Test {

    fun foo() {
        thread {
            synchronized(this) {
                println("foo")
                println(Thread.currentThread().name)
            }
        }
        LongAdder()
    }

    @Synchronized
    fun bar() {
        thread {
            foo()
            println(Thread.currentThread().name)
        }
    }
}


private val list = urls
    .map {
        Image(
            id = Random.nextInt(),
            it
        )
    }
