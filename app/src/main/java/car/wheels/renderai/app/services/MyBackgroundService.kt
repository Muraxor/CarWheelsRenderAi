package car.wheels.renderai.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.Service.START_STICKY
import android.content.Intent
import android.content.pm.ServiceInfo
import android.content.pm.ServiceInfo.FLAG_STOP_WITH_TASK
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import car.wheels.renderai.app.ui.MainActivity

class MyBackgroundService : Service() {

    // ID канала и уведомления (должен быть уникальным)
    private val CHANNEL_ID = "my_service_channel"
    private val NOTIFICATION_ID = 1

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        // 1. Создаем канал для уведомления (важно для Android 8.0+)
        createNotificationChannel()
        // 2. Создаем само уведомление

        // 3. Запускаем сервис в режиме foreground (Вот это ключевой момент!)
        //startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        println("onStartCommand $startId")
        // Здесь можно выполнять вашу работу
        // Если сервис все же убьют, система попытается перезапустить его с START_STICKY
        val notification = createNotification()
        // Повторно показываем уведомление на случай, если сервис был перезапущен системой
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        // Этот метод вызовется, когда пользователь смахнет приложение из списка Recents
        Log.w("Service", "Пользователь закрыл приложение через многозадачность!")
        // Здесь можно отправить "прощальный" сигнал на сервер, если нужно.
        // У вас есть около 10 секунд на выполнение кода.
        stopSelf() // Останавливаем сервис, так как задача удалена
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Название канала",
                NotificationManager.IMPORTANCE_LOW // Низкая важность, без звука
            ).apply {
                description = "Описание канала"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): android.app.Notification {
        // Создаем Intent для открытия приложения при клике на уведомление
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Сервис активен")
            .setContentText("Приложение работает в фоне")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Иконка
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW) // Не мешаем пользователю
            .build()
    }
}