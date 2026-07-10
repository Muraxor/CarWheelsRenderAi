package com.example.livecodeimproved.coroutines.limitedparalleldownloader

import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val downloader = LimitedParallelDownloaderImpl()

    downloader.downloadAll(urls, 2).also {
        println(it)
    }
}

val urls: List<String> = List(20) { index ->
    "https://example.com/image-${index + 1}-${kotlin.random.Random.nextInt(1_000_000)}.jpg"
}