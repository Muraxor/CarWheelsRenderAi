package car.wheels.renderai.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {

    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is HomeUiState.Success) {
        HomeScreen(
            items = (items as HomeUiState.Success).data,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    items: List<String>,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Home") })
        },
        bottomBar = {
            BottomBarNavigation()
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                var nameMyModel by remember { mutableStateOf("Compose") }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = nameMyModel,
                        onValueChange = { nameMyModel = it }
                    )
                }
            }
        }
    )

}

@Composable
private fun BottomBarNavigation() {
    NavigationBar {
        var selectedItem by remember { mutableIntStateOf(0) }
        val items = listOf("Songs", "Artists", "Playlists")
//        val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Star)
//        val unselectedIcons =
//            listOf(Icons.Outlined.Home, Icons.Outlined.FavoriteBorder, Icons.Outlined.StarBorder)
//
//        NavigationBar {
//            items.forEachIndexed { index, item -&gt;
//                NavigationBarItem(
//                    icon = {
//                        Icon(
//                            if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
//                            contentDescription = item,
//                        )
//                    },
//                    label = { Text(item) },
//                    selected = selectedItem == index,
//                    onClick = { selectedItem = index },
//                )
//            }
//        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MaterialTheme {
        HomeScreen(listOf("Compose", "Room", "Kotlin"))
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MaterialTheme {
        HomeScreen(listOf("Compose", "Room", "Kotlin"))
    }
}