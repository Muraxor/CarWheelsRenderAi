package com.example.benchmark

import android.content.Intent
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class ChatListBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scrollChatList() {
        benchmarkRule.measureRepeated(
            packageName = TARGET_PACKAGE_NAME,
            metrics = listOf(FrameTimingMetric()),
            iterations = 5,
            setupBlock = {
                pressHome()
                val intent = Intent(Intent.ACTION_MAIN)
                    .setPackage(TARGET_PACKAGE_NAME)
                    .addCategory(Intent.CATEGORY_LAUNCHER)
                startActivityAndWait(intent)
                waitForChatListToLoad()
            }
        ) {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val chatList = device.findObject(By.res(TARGET_PACKAGE_NAME, CHAT_LIST_TAG))

            assertNotNull("Chat list was not found", chatList)

            repeat(5) {
                chatList.swipe(Direction.UP, 0.6f, 300)
                device.waitForIdle()

                chatList.swipe(Direction.DOWN, 0.6f, 300)
                device.waitForIdle()
            }
        }
    }

    private fun MacrobenchmarkScope.waitForChatListToLoad() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val chatList = device.wait(
            Until.findObject(By.res(TARGET_PACKAGE_NAME, CHAT_LIST_TAG)),
            5_000
        )

        assertNotNull("Chat list did not load", chatList)
    }

    private companion object {
        const val TARGET_PACKAGE_NAME = "car.wheels.renderai"
        const val CHAT_LIST_TAG = "chat_list"
    }
}
