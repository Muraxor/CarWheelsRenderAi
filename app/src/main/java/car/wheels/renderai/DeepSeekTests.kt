package car.wheels.renderai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Stable
data class TodoItem(
    val id: Long,
    val title: String,
    val isCompleted: Boolean
)

@Composable
fun TodoApp() {
    val todos = remember { mutableStateListOf<TodoItem>() }
    var isLoading by remember { mutableStateOf(false) }

    TodoScreen(
        todos = todos,
        isLoading = isLoading,
        onAddTodo = { title ->
            todos.add(TodoItem(
                id = System.currentTimeMillis(),
                title = title,
                isCompleted = false
            ))
        },
        onToggleComplete = { id ->
            val index = todos.indexOfFirst { it.id == id }
            todos[index] = todos[index].copy(isCompleted = !todos[index].isCompleted)
        },
        onLoadData = {
            isLoading = true
        }
    )

    LaunchedEffect(isLoading) {
        for (i in 1..100) {
            delay(10)
            todos.add(TodoItem(System.currentTimeMillis() + i, "Task $i", false))
        }
    }
}

// Основной экран с State Hoisting
@Composable
private fun TodoScreen(
    todos: List<TodoItem>,
    isLoading: Boolean,
    onAddTodo: (String) -> Unit,
    onToggleComplete: (Long) -> Unit,
    onLoadData: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    // Derived state - вычисляемое состояние
    val completedCount by remember(todos) {
        derivedStateOf { todos.count { it.isCompleted } }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Прогресс: $completedCount/${todos.size}",
            style = MaterialTheme.typography.titleMedium
        )

        Row {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onAddTodo(text)
                        text = ""
                    }
                }
            ) {
                Text("Добавить")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        // Composition Local - передача контекста
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
            TodoList(
                todos = todos,
                onToggleComplete = onToggleComplete
            )
        }

        Button(onClick = onLoadData) {
            Text("Загрузить данные")
        }
    }
}

// LazyColumn с стабильными ключами
@Composable
private fun TodoList(
    todos: List<TodoItem>,
    onToggleComplete: (Long) -> Unit
) {
    LazyColumn {
        items(
            items = todos,
            key = { it.id } // Стабильные ключи
        ) { todo ->
            TodoItemRow(
                todo = todo,
                onToggleComplete = onToggleComplete
            )
        }
    }
}

// Переиспользуемый компонент с ленивой композицией
@Composable
private fun TodoItemRow(
    todo: TodoItem,
    onToggleComplete: (Long) -> Unit
) {
    // remember с ключами для стабильности
    val backgroundColor by remember(todo.id) {
        mutableStateOf(
            if (todo.isCompleted) Color(0xFFE8F5E9) else Color.White
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = { onToggleComplete(todo.id) }
        )

        Text(
            text = todo.title,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = if (todo.isCompleted) Color.Gray else Color.Black
            )
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    TodoApp()
}