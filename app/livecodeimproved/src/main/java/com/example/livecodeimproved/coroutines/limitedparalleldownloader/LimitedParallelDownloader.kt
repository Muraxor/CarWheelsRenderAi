package com.example.livecodeimproved.coroutines.limitedparalleldownloader

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlin.random.Random

interface LimitedParallelDownloader {

    suspend fun downloadAll(urls: List<String>, parallelism: Int): List<com.example.livecodeimproved.coroutines.limitedparalleldownloader.DownloadResult>
}

/**
 * Требования:
 *
 * Одновременно должно выполняться не больше parallelism загрузок;
 * результат должен содержать все url;
 * порядок результатов должен совпадать с порядком urls;
 * загрузку имитировать через delay(Random.nextLong(100, 700));
 * если один url “упал”, остальные должны продолжить работу;
 * вместо реального падения: если url содержит "bad", вернуть ошибочный результат в отдельной модели.
 */
class LimitedParallelDownloaderImpl : LimitedParallelDownloader {

    override suspend fun downloadAll(
        urls: List<String>,
        parallelism: Int
    ): List<DownloadResult> {
        return supervisorScope {
            val semaphore = Semaphore(parallelism)
            val dispatcher = Dispatchers.Default

            urls.map {
                async(dispatcher) {
                    semaphore.withPermit {
                        download(it)
                    }
                }
            }.awaitAll()
        }
    }
}

suspend fun download(url: String) = try {
    println("Start loading $url")
    delay(Random.nextLong(1000))

    val randomError = Random.nextInt(2)

    val download = when (randomError) {
        1 -> SuccessDownload(
            url = url,
            content = "Content $url"
        )

        0 -> throw RuntimeException("DownloadError $url")
        else -> throw RuntimeException("Generate problem $url")
    }
    download.also {
        println("$download loaded")
    }
} catch (e: Exception) {
    if (e is CancellationException) throw e
    ErrorDownload(
        message = e.message
    )
}


sealed class DownloadResult

data class SuccessDownload(
    val url: String,
    val content: String
) : DownloadResult()

data class ErrorDownload(
    val message: String?
) : DownloadResult()
