package com.example.simgplechatexample.presentation.test.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.simgplechatexample.presentation.test.login.state.Authorized
import com.example.simgplechatexample.presentation.test.login.state.Loading
import com.example.simgplechatexample.presentation.test.login.state.LoginState
import com.example.simgplechatexample.presentation.test.login.state.UnAuthorized
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Есть экран логина.
 *
 * После успешной авторизации необходимо через 2 секунды автоматически перейти на главный экран.
 *
 * Во время этих двух секунд пользователь может:
 *
 * изменить тему приложения;
 * изменить язык приложения;
 * повернуть экран;
 * повторно открыть этот экран после ухода с него.
 *
 * Навигация должна произойти корректно.
 */
@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onLoginState by rememberUpdatedState(onLogin)

    LaunchedEffect(state) {
        if (state is Authorized) {
            delay(2000)
            onLoginState()
        }
    }

    Column {
        Button(
            enabled = state !is Loading && state !is Authorized,
            onClick = {
                viewModel.login()
            }
        ) {
            Text("Login")
        }
    }
}

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(UnAuthorized)
    val state = _state.asStateFlow()

    fun login() {
        viewModelScope.launch {
            try {
                _state.update { Loading }
                delay(2000)
                _state.update {
                    Authorized
                }
            } catch (e: Exception) {
                _state.update {
                    UnAuthorized
                }
            }
        }
    }
}