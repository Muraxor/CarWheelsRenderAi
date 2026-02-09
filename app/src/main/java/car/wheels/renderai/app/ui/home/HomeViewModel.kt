package car.wheels.renderai.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import car.wheels.renderai.data.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    testRepository: TestRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = testRepository
        .myModels.map<List<String>, HomeUiState> { HomeUiState.Success(data = it) }
        .catch { emit(HomeUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState.Loading)
}

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Error(val throwable: Throwable) : HomeUiState
    data class Success(val data: List<String>) : HomeUiState
}