package car.wheels.renderai.core

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TavernViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    // Состояния (приватные изменяемые, публичные неизменяемые)
    private val _counter = MutableStateFlow(savedStateHandle["counter"] ?: 0)
    val counter: StateFlow<Int> = _counter

    private val _name = MutableStateFlow(savedStateHandle["name"] ?: "")
    val name: StateFlow<String> = _name

    private val _currentScreen = MutableStateFlow(savedStateHandle["currentScreen"] ?: "stats")
    val currentScreen: StateFlow<String> = _currentScreen

    val totalPrice: Int
        get() = _counter.value * 150

    // Методы (бизнес-логика)
    fun orderBeer() {
        _counter.value++
        savedStateHandle["counter"] = _counter.value
        println("Заказано пиво! Всего заказов: ${_counter.value}")
    }

    fun resetOrders() {
        _counter.value = 0
        savedStateHandle["counter"] = 0
        println("Счётчик обнулён")
    }

    fun updateName(newName: String) {
        _name.value = newName
        savedStateHandle["name"] = _name.value
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
        savedStateHandle["currentScreen"] = _currentScreen.value
    }
}

class TavernViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()
        return TavernViewModel(savedStateHandle) as T
    }
}