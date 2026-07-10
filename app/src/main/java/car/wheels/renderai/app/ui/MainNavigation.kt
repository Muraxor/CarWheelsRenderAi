package car.wheels.renderai.app.ui

import android.content.Intent
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
import com.example.simgplechatexample.presentation.chat.ChatScreen
import com.example.simgplechatexample.presentation.chatlist.ChatListScreen
import com.example.simgplechatexample.presentation.test.login.LoginScreen
import com.example.simgplechatexample.presentation.tasks.Screen
import com.example.simgplechatexample.presentation.test.TestScreens

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "test") {
        composable(route = "todoApp") {
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

        composable("chat_list") {
            ChatListScreen(
                onChatClick = { chatId ->
                    navController.navigate("chat_detail/$chatId")
                }
            )
        }

        composable("chat_detail/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
            // Здесь экран сообщений (твой существующий ChatScreen)
            ChatScreen(chatId = chatId)
        }

        composable("test") {
            TestScreens(
                onLogin = { navController.navigate("chat_list") }
            )
        }

        composable("plan") {
            Screen()
        }
    }
}