package car.wheels.renderai.app.ui

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import car.wheels.renderai.TodoApp
import car.wheels.renderai.core.TavernApp
import com.example.simgplechatexample.presentation.chatlist.ChatScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "chat") {
        composable(route = "test") {
            TodoApp()
        }
        composable("main") {
            TavernApp()
        }

        composable("test1") {
            val context = LocalContext.current
            Button(
                onClick = {
                    val intent = Intent(context, TestActivity::class.java).apply {
                        setFlags(FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                }
            ) {
                Text("ЖМИ")
            }
        }

        composable("chat") {
            ChatScreen()
        }
    }
}