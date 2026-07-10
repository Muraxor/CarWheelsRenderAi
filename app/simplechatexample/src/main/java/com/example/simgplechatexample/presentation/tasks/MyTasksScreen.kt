package com.example.simgplechatexample.presentation.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.random.Random

data class Dog(
    val breed: String = "sheap"
) {

    var name: String = ""
}

fun main() {
    val hashMap = hashMapOf<Dog, Dog>()
    val dog1 = Dog().apply { name = "Joe" }
    val dog2 = Dog("Tolik")
    val dog3 = Dog().apply { name = "Dimon" }

    hashMap[dog1] = dog1
    hashMap[dog2] = dog2
    hashMap[dog3] = dog3

    println(hashMap.toList())
}

@Composable
fun Screen() {
    var count by remember { mutableIntStateOf(0) }
    Button(
        onClick = { count++ }
    ) { Test(count) }

}

data class User(
    val id: Int,
    val name: String,
)

@Composable
fun Test(count: Int) {
    Text("$count")

    SideEffect {
        println("side $count")
    }

    println("Before")
}

@Composable
fun Child(user: User) {
    println("Child $user")
    Text(user.name)
}

@Composable
fun MyTaskScreen(viewModel: MyTasksViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Surface() {
        Column() {
            Header()

            when (state) {
                is Content -> {
                    (state as Content).also { content ->
                        TaskProgressCard(content.completed, content.total, content.progress)
                    }
                }

                else -> {

                }
            }
        }
    }
}

@Composable
fun Header(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Мои задачи")
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = Color.Black
        )
    }
}

@Composable
fun TaskProgressCard(
    completed: Int,
    total: Int,
    progress: Progress,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(
            vertical = 8.dp,
            horizontal = 16.dp
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row() {
            CircularProgressWithText(progress)
            Text("Выполнено $completed из $total")
        }
    }
}


@Preview
@Composable
fun TaskProgressCardPreview() {
    TaskProgressCard(1, 5, Progress(0.5f, "1/5"))
}

@Preview
@Composable
fun HeaderPreview() {
    Header()
}

@Preview
@Composable
fun MyTasksScreenPreview() {
    MyTaskScreen()
}

@Composable
fun CircularProgressWithText(
    progress: Progress,
    modifier: Modifier = Modifier
) {
    val safeProgress = progress.value.coerceIn(0f, 1f)

    Box(
        modifier = modifier.size(96.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { safeProgress },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 8.dp
        )

        Text(
            text = progress.text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}