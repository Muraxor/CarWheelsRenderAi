package com.example.benchmark

import android.content.Intent
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test


class ChatListBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    private val packageName = InstrumentationRegistry.getInstrumentation()
        .targetContext.packageName

    @Test
    fun scrollChatList() {
        benchmarkRule.measureRepeated(
            packageName = packageName,
            metrics = listOf(
                FrameTimingMetric(), // Замеряет FPS и дропнутые кадры
                StartupTimingMetric()  // если хочешь замерить холодный старт
            ),
            iterations = 5, // Чем больше, тем точнее, но дольше
            setupBlock = {
                // 1. Закрываем приложение
                pressHome()

                // 2. Открываем через Intent (холодный старт)
                val intent = Intent(Intent.ACTION_MAIN)
                    .setPackage(packageName)
                    .addCategory(Intent.CATEGORY_LAUNCHER)
                startActivityAndWait(intent)

                // 3. Ждём, пока загрузится список
                waitForChatListToLoad()
            }
        ) {
            // 4. СЦЕНАРИЙ: скроллим список туда-сюда
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

            // Ищем корневой ComposeView
            val composeView = device.findObject(
                androidx.test.uiautomator.By
                    .clazz("androidx.compose.ui.platform.ComposeView")
                    .pkg(packageName)
            )

            // Если не нашёлся — ищем RecyclerView (для гибридных экранов)
            // val listView = device.findObject(By.clazz("androidx.recyclerview.widget.RecyclerView"))

            // Скроллим 5 раз вниз-вверх
            repeat(5) { scrollIteration ->
                // Свайп снизу вверх (скролл вниз)
                composeView.swipe(
                    Direction.DOWN,
                    0.6f,
                    300 // Пикселей в секунду
                )
                device.waitForIdle()

                // Свайп сверху вниз (скролл вверх)
                composeView.swipe(
                    Direction.UP,
                    0.6f,
                     300
                )
                device.waitForIdle()
            }
        }
    }

    // Вспомогательная функция: ждём появления списка
    private fun MacrobenchmarkScope.waitForChatListToLoad() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Ждём, пока появится любой элемент с текстом (заглушка)
        // ИЛИ ждём конкретный ID элемента
        device.wait(
            Until.findObject(By.res(packageName, "chat_list_content")), // ID из твоего ресурса
            5_000 // 5 секунд таймаут
        )

        // Альтернатива: ждём, пока пропадёт ProgressBar
        // device.wait(Until.gone(By.clazz("android.widget.ProgressBar")), 5_000)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun scrollChatListWithComposeTest() {
        benchmarkRule.measureRepeated(
            packageName = packageName,
            metrics = listOf(FrameTimingMetric()),
            iterations = 3,
            setupBlock = {
                pressHome()
                startActivityAndWait()
                waitForChatListToLoad()
            }
        ) {
            // Используем ComposeTestRule для точного контроля
            val composeRule = createComposeRule()

            composeRule.apply {
                // Находим LazyColumn по тегу
                val lazyColumn = onNodeWithTag("chat_list")

                // Скроллим
                repeat(3) {
                    lazyColumn.performScrollToIndex(50) // Скролл к 50-му элементу
                    waitForIdle()
                    lazyColumn.performScrollToIndex(0) // Наверх
                    waitForIdle()
                }
            }
        }
    }
}