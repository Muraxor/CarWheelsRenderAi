package car.wheels.renderai.app

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RaceCondition(val counter: Counter) {

    fun start(): ExecutorService {
        val executors = Executors.newCachedThreadPool()

        val runnable1 = Runnable {
            for(i in 0..999) {
                counter.inc()
            }
        }

        val runnable2 = Runnable {
            for (i in 0..999) {
                counter.dec()
            }
        }

        executors.execute(runnable1)
        executors.execute(runnable2)

        executors.execute {
            //Thread.sleep(100)
            println("RaceCondition ${counter.count}")
        }

        MyClass.instanceThreadSafe
        return executors
    }
}

class Counter(start: Long){

    var count: Long = start
        private set


    @Synchronized
    fun inc() {
        count++
    }

    @Synchronized
    fun dec() {
        count--
    }
}

class MyClass {
    companion object {
        val instance: MyClass by lazy { MyClass() }  // безопасно, synchronized по умолчанию
        val instanceThreadSafe: MyClass by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MyClass() }
    }
}