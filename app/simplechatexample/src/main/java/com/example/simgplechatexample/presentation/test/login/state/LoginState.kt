package com.example.simgplechatexample.presentation.test.login.state

sealed interface LoginState

data object Loading : LoginState

data class Error(val message: String) : LoginState

data object Authorized : LoginState

data object UnAuthorized : LoginState
