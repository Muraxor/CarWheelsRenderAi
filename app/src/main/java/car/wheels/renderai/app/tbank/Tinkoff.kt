package car.wheels.renderai.app.tbank

import android.content.Context
import car.wheels.renderai.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.LinkedList

const val PARAM_DELAY_KEY = "param_delay_key"
const val TIME_ZERO_TRASHHOLD = 0L

open class BotProfiler(
    private val analyticManager: AnalyticManager,
) {

    var startTime: Long = 0



        @Volatile
        var identifierIds: LinkedList<String> = LinkedList()

    fun init(identifierId: String) {
        identifierIds[0] = identifierId

        clearWaiting()
    }

    @Synchronized
    fun onMessageReceived() {
        var tempTime = startTime
        val delay = System.currentTimeMillis() - tempTime

        if(identifierIds.first() != null && delay != 0L) {

            identifierIds.forEach {
                analyticManager.sendAnswerDelay(delay)
            }

            clearWaiting()
        }
    }

    fun onSendMessage() {
        if(identifierIds.first() == null) return

        if(startTime != TIME_ZERO_TRASHHOLD) {
            startTime = System.currentTimeMillis()
        }
    }

    fun clearWaiting() {
        startTime = 0
    }
}

class AnalyticManager(
    private var context: Context,
    private val anatilycSpec: AnatilycSpec
) {

    fun sendAnswerDelay(delay: Long) {
        val keyPrefix = context.getString(R.string.app_name)
        val params = mapOf(keyPrefix + PARAM_DELAY_KEY to delay)
        anatilycSpec.sendEvent(params)
    }
}

interface AnatilycSpec {
    fun sendEvent(params: Map<String, Any>)
}

fun main() = runBlocking {
    launch {
        delay(1000)
        println("A")
    }
    launch {
        delay(500)
        println("B")
    }
    println("Start")
}