package car.wheels.renderai.app

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.seconds


val scope = CoroutineScope(Job())

open class Animal  {
    open val name: String = "Animal"

    open fun blowJob() {

            GlobalScope.launch {
                delay(1000)
                println(name)
            }

    }

    init {
        blowJob()
    }
}

open class Dog: Animal() {
    override val name : String = "DOG"

    override fun blowJob() {
        super.blowJob()
    }
}

class Dimon(
    val t: Int
): Dog() {

    val s = 1


    val d: Int = 2
}

fun main() = runBlocking {

    Dimon(1).blowJob()
    delay(5000)
//    val windows = async(Dispatchers.IO) { order(1) }
//    val doors = async(Dispatchers.IO) { order(2).also { cancel() } }
//
//    launch(Dispatchers.Default) {
//        perform("Bricks")
//        launch { perform("Install ${windows.await()}") }
//        launch { perform("Install ${doors.await()}") }
//    }.join()
//
//    println(doors.isCancelled)
//    println(doors.isCompleted)

    //t()



    Unit
}

suspend fun t() {
    var job: Job = Job()
    var scope1: CoroutineScope
    val scope = coroutineScope {
        launch {
            println("START")
            scope1 = CoroutineScope(Job(parent = scope.coroutineContext.job))
            job = scope1.launch {
                println("CHILD START")
                delay(4000)
                println("CHILD END")
            }

            println("END")
        }
    }

    delay(2000)
    scope.cancelAndJoin()
    //job.join()
}

suspend fun order(value: Int): Int {
    println("START $value ORDER")
    delay(1000)
    println("END $value ORDER")

    return value
}

suspend fun perform(name: String) {
    println("START $name TASK")
    delay(1000)
    println("END $name TASK")
}

fun DimaLoH() = runBlocking {
    val handler = CoroutineExceptionHandler { _, throwable ->
        println("HANDLER: ${throwable.message}")
    }

    supervisorScope {
        launch(handler) {
            val first = launch {
                throw RuntimeException("First failed")
            }

            val second = async {
                try {
                    delay(1000)
                    println("SECOND")
                    "OK" //4
                } finally {
                    println("SECOND finally")
                }
            }

            coroutineScope {
                launch {
                    delay(200)
                    throw IllegalStateException("Inner launch failed")
                }

                try {
                    first.join()
                } catch (e: RuntimeException) {
                    println("FIRST caught")
                }

                println("INNER END")
            }

            println(second.await())
        }

        delay(1500)
        println("OUTER END") //6
    }

    println("END")
}

fun updateProgressBar(value: Int, marker: String) {
    print(marker)
}

suspend fun a() {
    val singleThreadedDispatcher = Dispatchers.Default.limitedParallelism(1)
    withContext(singleThreadedDispatcher) {
        launch {
            repeat(5) {
                updateProgressBar(it, "A")
                yield()
            }
        }
        launch {
            repeat(5) {
                updateProgressBar(it, "B")
                yield()
            }
        }
        println()
    }
}

private fun testTime(lamdda: () -> Unit) {
    val start = System.currentTimeMillis()
    println("Start  ${start}")
    lamdda()
    println("End  ${System.currentTimeMillis() - start}")
}

private fun doInThreads() = repeat(10) {
    Thread { doLongJob() }.start()
}

private fun doInCoroutines() = repeat(10) {
    scope.launch(Dispatchers.Main) { doLongJob() }
}


fun doLongJob() {
    val list = mutableListOf<Int>()
    for (i in 0..1000000) {
        list.add(i)
    }
}