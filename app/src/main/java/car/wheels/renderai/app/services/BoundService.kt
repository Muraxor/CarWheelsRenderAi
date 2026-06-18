package car.wheels.renderai.app.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Random

class BoundService : Service() {

    companion object {
        private const val TAG = "BoundService"
    }

    // Binder, который будет возвращен клиенту
    private val binder = LocalBinder()

    // Coroutine scope для фоновых задач
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Данные сервиса
    private var randomNumber = 0
    private var isGenerating = false

    // Callback для уведомления Activity об изменениях
    private var callback: ServiceCallback? = null

    // Интерфейс для обратной связи с Activity
    interface ServiceCallback {
        fun onNumberUpdated(number: Int)
        fun onGenerationStarted()
        fun onGenerationStopped()
    }

    // Binder, предоставляющий доступ к методам сервиса
    inner class LocalBinder : Binder() {
        fun getService(): BoundService = this@BoundService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Сервис создан")
        startNumberGeneration()
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: Клиент подключился к сервису")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: Клиент отключился")
        return true // true означает, что мы хотим получить вызов onRebind при следующем подключении
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind: Клиент переподключился")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Сервис уничтожен")
        serviceScope.cancel() // Останавливаем все корутины
        callback = null
    }

    // ========== Публичные методы для клиентов ==========

    fun setCallback(callback: ServiceCallback?) {
        this.callback = callback
    }

    fun getRandomNumber(): Int {
        return randomNumber
    }

    fun startNumberGeneration() {
        if (isGenerating) return
        isGenerating = true
        callback?.onGenerationStarted()

        serviceScope.launch {
            while (isGenerating && isActive) {
                delay(1000) // Генерируем число каждую секунду
                randomNumber = Random().nextInt(100)
                Log.d(TAG, "Сгенерировано число: $randomNumber")

                // Обновляем UI в главном потоке
                withContext(Dispatchers.Main) {
                    callback?.onNumberUpdated(randomNumber)
                }
            }
        }
    }

    fun stopNumberGeneration() {
        isGenerating = false
        callback?.onGenerationStopped()
        Log.d(TAG, "Генерация чисел остановлена")
    }

    fun performLongOperation(onComplete: (String) -> Unit) {
        serviceScope.launch {
            delay(2000) // Симуляция долгой операции
            val result = "Операция завершена. Последнее число: $randomNumber"
            withContext(Dispatchers.Main) {
                onComplete(result)
            }
        }
    }
}