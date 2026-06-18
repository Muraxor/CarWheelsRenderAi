import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livecodeimproved.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * @see car.wheels.renderai.app.tbank.ScreenViewModelOriginal
 */
internal class ScreenViewModelImproved(
    val configRepository: ConfigRepository,
    val holder: UserHolder,
    val cardRepository: CardRepository,
    val analytics: AnalyticsService,
    val resourceProvider: ResourceProvider
) : ViewModel() {
    private lateinit var config: Config

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    var successfulChecks = 0L

    init {
        observeUiState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeUiState() {
        cardRepository.observeAvailableCard()
            .flatMapLatest {
                flowOf(configRepository.loadConfig() to it)
            }
            .onEach {
                _uiState.update { UiState.Loading }
            }
            .filter { (config, type) ->
                when {
                    type == CardType.PREMIUM && holder.getUserScore() > 42 -> true
                    config.specialUsers.contains(holder.getUserId()) -> true
                    else -> false
                }
            }
            .flatMapConcat { (_, cardType) ->
                blackListFlow(cardType)
            }
            .onEach {
                _uiState.update {
                    UiState.UiModel(
                        title = card.resolveTitle(config, resourceProvider),
                        description = resourceProvider.getString(
                            R.string.description_pattern,
                            config.kinderSurprizeCashback
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun blackListFlow(cardType: CardType): Flow<BlackList> = flowOf(
        configRepository.checkBlackList(cardType, holder.getUserId())
            .apply {
                successfulChecks++
            }
    )
        .catch { error ->
            if (error is ForbiddenCardTypeException) {
                _uiState.update {
                    UiState.Error(resourceProvider.getString(R.string.ForbiddenCardTypeException))
                }
            }
        }

    //var disposables: CompositeDisposable? = CompositeDisposable()

    override fun onCleared() {
        //check
        viewModelScope.launch {
            withContext(NonCancellable) { // либо отправлять триггер в другйо класс, у которого свой скоуп, который живет дольше чем эта ViewModel
                analytics.sendSuccessfulChecks(successfulChecks)
            }
        }
        super.onCleared()
    }
}

abstract class Config(
    open val debitTitle: String,
    open val creditTitle: String,
    open val kidTitle: String,
    open val kinderSurprizeCashback: Long,
    open val specialUsers: List<Long>
)

interface ConfigRepository {
    suspend fun loadConfig(): Config

    /**
     * Checks all user's data for suspicious activity, can take up to 30 seconds.
     * @throws ForbiddenCardTypeException if user is not eligible for that card type
     */
    fun checkBlackList(cardType: CardType, userID: Long): BlackList

}

interface BlackList {
    var successfulChecks: Long
}

interface CardRepository {
    /**
     * Fetches most relevant available cards, can change in any minute to a premium version!
     */
    fun observeAvailableCard(): Flow<CardType>
}

interface UserHolder {
    fun getUserId(): Long
    fun getUserScore(): Long
}

interface AnalyticsService {
    @FormUrlEncoded
    @POST("v1/analytics")
    suspend fun sendSuccessfulChecks(checks: Long)
}

enum class CardType {
    DEBIT {
        override fun resolveTitle(config: Config, resourceProvider: ResourceProvider) =
            config.debitTitle
    },
    CREDIT {
        override fun resolveTitle(config: Config, resourceProvider: ResourceProvider) =
            config.creditTitle
    },
    KID {
        override fun resolveTitle(config: Config, resourceProvider: ResourceProvider) =
            config.kidTitle
    },
    PREMIUM {
        override fun resolveTitle(config: Config, resourceProvider: ResourceProvider) =
            resourceProvider.getString(R.string.congratulations)
    };

    abstract fun resolveTitle(config: Config, resourceProvider: ResourceProvider): String
}

sealed class UiState {

    data object Loading : UiState()
    data class Error(val text: String) : UiState()
    data class UiModel(val title: String, val description: String) : UiState()

}

class ForbiddenCardTypeException : Exception()

interface ResourceProvider {
    fun getString(@StringRes id: Int): String

    fun getString(@StringRes id: Int, vararg formatArgs: Any): String
}
