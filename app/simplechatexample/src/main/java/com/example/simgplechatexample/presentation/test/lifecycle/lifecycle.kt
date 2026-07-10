package com.example.simgplechatexample.presentation.test.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Нужно подписаться на события жизненного цикла Activity.
 *
 * Когда экран уничтожается, подписка должна корректно сниматься.
 *
 * Во время работы приложения Activity может пересоздаваться.
 */
@Composable
fun LifecycleScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current

    var state by remember { mutableStateOf("Loading..") }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            state = event.name
            println(event.name)
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column {
        Text(state)
    }
}