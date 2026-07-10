package com.example.livecodeimproved.coroutines.imageprocessingpipeline.channels

import com.example.livecodeimproved.core.di.tryCatch
import com.example.livecodeimproved.core.di.tryCatchIgnoreError
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.DefaultImageMethodsImpl
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.Bitmap
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.DownloadedImage
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.Image
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.SavedImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ChannelsImageProcessingPipelineImpl : DefaultImageMethodsImpl {

    override suspend fun processImages(images: List<Image>): List<SavedImage> = supervisorScope {
        val downloadedChannel = Channel<DownloadedImage>(500)
        val decodedChannel = Channel<Bitmap>(500)

        val downloadJobs = images.map { image ->
            launch {
                tryCatch(
                    {
                        val result = download(image)
                        downloadedChannel.send(result)
                    },
                    {
                        //handle error
                    }
                )
            }
        }

        val decodeJob = launch(Dispatchers.Default) {
            for (downloaded in downloadedChannel) {
                tryCatchIgnoreError {
                    val bitmap = decode(downloaded)
                    decodedChannel.send(bitmap)
                }
            }
        }

        val saveDeferred = async {
            saveWork(decodedChannel)
        }

        downloadJobs.joinAll()
        downloadedChannel.close()

        decodeJob.join()
        decodedChannel.close()

        val result = saveDeferred.await()

        result
    }

    private suspend fun saveWork(decodedChannel: ReceiveChannel<Bitmap>): List<SavedImage> {
        val savedImages = mutableListOf<SavedImage>()

        for (decoded in decodedChannel) {
            tryCatchIgnoreError {
                val saved = save(decoded)
                savedImages.add(saved)
            }
        }

        return savedImages
    }
}