package car.wheels.renderai.app.tbank

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Tinkoff2(
    val urlHelper: UrlHelper
) {


    suspend fun test(chapters: List<Chapter>) {
        var i = chapters.size

        val mutex = Mutex()
        val channel = Channel<Unit>()

        mutex.lock()

        urlHelper.getUrls()
            .asFlow()
            .onEach {
                cacheImages(it)
            }
            .collect {  }

        val flows = urlHelper.getUrls()
            .map {
                flow {
                    cacheImages(it)
                    emit(Unit)
                }
            }.toList()

        var job: Job? = null

        job = GlobalScope.launch(Dispatchers.Default) {
            flows.forEach {
                it.collect {}
                i--
            }

            if (i == 0) {
                channel.send(mutex.unlock())
            }
        }


        mutex.withLock {  }

        job.join()
    }


    suspend fun cacheImages(url: String) {
        // TODO:
    }
}

class Chapter(
    val id: String
)

interface UrlHelper {

    fun getUrls(): List<String>
    fun getUrlsFlow(): Flow<String>
}
