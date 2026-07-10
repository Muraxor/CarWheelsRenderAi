package com.example.livecodeimproved.coroutines.imageprocessingpipeline

import com.example.livecodeimproved.core.di.StubConfig
import com.example.livecodeimproved.core.di.stubResult
import com.example.livecodeimproved.core.di.tryCatch
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.DownloadedImage
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.Image
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.SavedImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.nextInt

class AsyncImageProcessingPipelineImpl : DefaultImageMethodsImpl {
    /**
     * Почему?
     *
     * Каждый download - HTTP Request. 1000 одновременно. Что произойдет?
     * - можно забить пул HTTP-соединений;
     * - можно словить rate limit;
     * - можно перегрузить сервер;
     * - можно получить тысячи открытых сокетов.
     */

    private val semaphore = Semaphore(5)
    override suspend fun processImages(images: List<Image>): List<SavedImage> = supervisorScope {
        images.map { image ->
            async {
                semaphore.withPermit {
                    process(image)
                }
            }
        }
            .awaitAll()
            .also { savedImages ->
                val count = savedImages.count { it == null }
                println("Error processing $count")
            }
            .filterNotNull()
    }

    private suspend fun process(image: Image) =
        tryCatch(
            {
                val downloaded = download(image)
                val decoded = withContext(Dispatchers.Default) {
                    decode(downloaded)
                }
                save(decoded)
            },
            { e ->
                e.printStackTrace()
                null
            }
        )
}

suspend fun downloadStub(image: Image): DownloadedImage {
    val downloadTime = Random.nextLong(300, 800)
    val config = StubConfig(downloadTime, "Download $image error", data = image)

    return stubResult(
        config = config
    ) {
        val size = Random.nextInt(100..10000)
        DownloadedImage(
            image.id,
            ByteArray(size)
        )
    }
}
