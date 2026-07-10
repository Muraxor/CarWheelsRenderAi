package com.example.simgplechatexample.presentation.test

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simgplechatexample.presentation.test.dimon.Screen
import com.example.simgplechatexample.presentation.test.lifecycle.LifecycleScreen
import com.example.simgplechatexample.presentation.test.login.LoginScreen

@Composable
fun TestScreens(
    onLogin: () -> Unit,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "dimon") {
        composable("login") {
            LoginScreen(
                onLogin = { onLogin() }
            )
        }

        composable("lifecycle") {
            LifecycleScreen()
        }

        composable("dimon") {
            Screen()
        }
    }
}