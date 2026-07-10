package com.example.livecodeimproved.coroutines.imageprocessingpipeline

import com.example.livecodeimproved.core.di.StubConfig
import com.example.livecodeimproved.core.di.stubResult
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.Bitmap
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.DownloadedImage
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.Image
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.SavedImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

interface DefaultImageMethodsImpl : ImageProcessingPipeline {

    override suspend fun download(image: Image): DownloadedImage {
        println("Start download $image")
        return withContext(Dispatchers.Default) {
            downloadStub(image)
        }.also {
            println("Donwload completed $image")
        }
    }

    override fun decode(image: DownloadedImage): Bitmap {
        println("Start decoding $image")
        Thread.sleep(300)
        return Bitmap(
            image.id
        ).also {
            println("Decoding completed for $image")
        }
    }

    override suspend fun save(bitmap: Bitmap): SavedImage {
        println("Start saving $bitmap")
        delay(Random.nextLong(100, 300))
        return stubResult(
            StubConfig(
                delay = Random.nextLong(100, 300),
                message = "Saving error $bitmap",
                data = bitmap
            )
        ) {
            SavedImage(
                id = bitmap.id
            ).also {
                println("$bitmap saved")
            }
        }
    }
}
