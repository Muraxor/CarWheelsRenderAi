package car.wheels.renderai.core

import android.app.DownloadManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.os.CountDownTimer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MovableContent
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UserProfileScreen(userName: String, userAge: Int) {
    // Display user profile information
    Column {
        Text(text = "Name: $userName")
        Text(text = "Age: $userAge")
    }

    // Use SideEffect to log an analytics event each time this composable recomposes
    SideEffect {
        println("UserProfileScreen" + "UserProfileRecomposed")
        //logAnalyticsEvent()
    }
}

@Composable
fun TavernApp() {
    val navController = rememberNavController()
    val viewModel: TavernViewModel = viewModel(
        factory = TavernViewModelFactory()
    )

    val name by viewModel.name.collectAsStateWithLifecycle()
    val counter by viewModel.counter.collectAsState()

    val s: Int by remember { mutableStateOf(1) }

    NavHost(
        navController = navController,
        startDestination = Screen.Tavern.route
    ) {
        composable(Screen.Tavern.route) {
            TavernScreen(
                name = name,
                counter = counter,
                onNameChange = { viewModel.updateName(it) },
                onOrderClick = { viewModel.orderBeer() },
                onResetClick = { viewModel.resetOrders() },
                onShowStats = { navController.navigate("${Screen.Stats.route}/$name") }
            )
        }
        composable(
            "${Screen.Stats.route}/{heroName}",
            arguments = listOf(navArgument("heroName") { type = NavType.StringType })
        ) { backStackEntry ->
            val heroName = backStackEntry.arguments?.getString("heroName") ?: ""
            StatsScreen(
                name = heroName,
                counter = counter,
                price = viewModel.totalPrice,
                onResetClick = { viewModel.resetOrders() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun StatsScreen(
    name: String,
    counter: Int,
    price: Int,
    onResetClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.wrapContentSize()) {
        Text("Всего заказов: $counter")
        Text("Последнее введённое имя: $name")
        Text("Общая стоимость: $price")
        Button(onClick = {
            onResetClick()
        }
        ) {
            Text("Очистить статистику")
        }
        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}

@Composable
fun TimerScreen(onTick: (Int) -> Unit) {
    var seconds by remember { mutableStateOf(0) }

    LaunchedEffect (Unit) {
        for(i in 0..1000) {
            delay(1000L)
            seconds++
        }
    }

    Text("Seconds: $seconds")
}

@Composable
fun TavernScreen(
    name: String,
    counter: Int,
    onNameChange: (String) -> Unit,
    onOrderClick: () -> Unit,
    onResetClick: () -> Unit,
    onShowStats: () -> Unit
) {
    val state = rememberScrollState()

    Column(Modifier
        .padding(16.dp)
        .verticalScroll(state)
    ) {
        Row(modifier = Modifier.width(IntrinsicSize.Min)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Короткий", modifier = Modifier.fillMaxWidth())
                Text("Очень длинный текст, который шире первого", modifier = Modifier.fillMaxWidth())
                Text("Средний", modifier = Modifier.fillMaxWidth())
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("1", modifier = Modifier.fillMaxWidth())
                Text("1000000", modifier = Modifier.fillMaxWidth())
                Text("42", modifier = Modifier.fillMaxWidth())
            }
        }

        TimerScreen({})

        Text(text = "Заказы для $name: $counter")

        TextField(
            value = name,
            onValueChange = {
                onNameChange.invoke(it)
            },
            label = { Text("Введите имя") },
        )

        Button(
            modifier = Modifier
                .padding(top = 8.dp),
            onClick = {
                onOrderClick()
            }
        ) {
            Text(text = "Заказать пиво")
        }

        DebouncedButton(
            modifier = Modifier
                .padding(top = 8.dp),
            onClick = {
                onResetClick()
            }
        ) {
            Text("Сброс")
        }

        if (counter > 5) {
            Text(text = "Осторожно, герой! Ты заказываешь много пива!")
        }

        Button(onClick = onShowStats) {
            Text("Показать статистику")
        }
    }
}

@Preview
@Composable
fun GreetingCardPreview() {
    TavernApp()
}

@Composable
fun DebouncedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isClickable by remember { mutableStateOf(true) }
    Button(
        onClick = {
            if (isClickable) {
                onClick()
                isClickable = false
            }
        },
        modifier = modifier,
        enabled = isClickable  // ← кнопка становится неактивной внешне
    ) {
        content()
    }

    val s = rememberLazyListState()
    s.firstVisibleItemIndex

    // Асинхронное включение обратно
    if (!isClickable) {
        LaunchedEffect(Unit) {
            delay(1000L)
            isClickable = true
        }
    }
}
