package car.wheels.renderai.core

sealed class Screen(val route: String) {
    object Tavern : Screen("tavern")
    object Stats : Screen("stats")
    object Settings : Screen("settings")  // новый экран для бонуса
}