package com.example.simgplechatexample.presentation.test.dimon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.thread


var a = 0
var b = 0

fun main(): Unit = runBlocking {

}


@Composable
fun Screen(
    viewModel: ScreenViewModel = hiltViewModel()
) {

    var show by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val buttonEnabled = !state.isLoading

    Column {
        Button(
            enabled = true,
            onClick = {
                show = !show
                //viewModel.onClickButton()
            }
        ) {
            Text("Click me")
        }



        if (show) {
            val scopeInside = rememberCoroutineScope()   // ← здесь
            Button(
                onClick = {
                    scopeInside.launch {
                        println("Start inside")
                        delay(1000)
                        println("End inside")
                    }
                }
            ) {
                Text("inside")
            }
        }
        val scope = rememberCoroutineScope()   // ← здесь
        Button(
            onClick = {
                scope.launch {
                    println("Start outside")
                    delay(1000)
                    println("End outside")
                }
            }
        ) {
            Text("outside")
        }


        Text("Count=${state.count}")
    }

}

@HiltViewModel
class ScreenViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

    fun onClickButton() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            delay(5000)
            _state.update {
                it.copy(count = it.count + 1, isLoading = false)
            }
        }
    }
}

sealed class UiState

data class ScreenState(
    val count: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
) : UiState()

