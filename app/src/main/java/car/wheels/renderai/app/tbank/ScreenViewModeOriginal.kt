package car.wheels.renderai.app.tbank

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import car.wheels.renderai.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.atomic.AtomicLong


class ScreenViewModelOriginal(
    var configs: ConfigRepository,
    var holder: UserHolder,
    var cards: CardRepository,
    var analytics: AnalyticsService,
    var context: Context
) : ViewModel() {
    lateinit var config: Config
    lateinit var card: CardType

    var liveData = MutableLiveData<UiModel>()

    var successfulChecks = 0L

    private val _checks = AtomicLong(0)

    fun increment() {
        _checks.incrementAndGet()
    }

    fun getAndReset(): Long {
        return _checks.getAndSet(0)
    }

    init {
        GlobalScope.launch {
            flowOf(configs.loadConfig()).zip(
                cards.observeAvailableCard(),
                { it1, it2 -> Pair(it1, it2) })
                .flowOn(Dispatchers.Default)
                .filter {
                    it.second == CardType.PREMIUM && holder.getUserScore() > 42
                            || it.second == CardType.PREMIUM
                            || it.first.specialUsers.contains(holder.getUserId())
                }
                .onEach {
                    config = it.first
                    card = it.second
                }
                .flatMapConcat {
                    flowOf(
                        configs.checkBlackList(it.second, holder.getUserId())
                            .apply {
                                successfulChecks++
                            }
                    )
                }
                .collect {
                    liveData.value = UiModel(
                        if (card == CardType.CREDIT) config.creditTitle
                        else if (card == CardType.DEBIT) config.debitTitle
                        else if (card == CardType.KID) config.kidTitle
                        else if (card == CardType.PREMIUM) context.getString(R.string.congratulations)
                        else "",
                        context.getString(
                            R.string.description_pattern,
                            config.kinderSurprizeCashback
                        )
                    )
                }
        }
    }

    //var disposables: CompositeDisposable? = CompositeDisposable()

    override fun onCleared() {
        GlobalScope.launch {
            analytics.sendSuccessfulChecks(successfulChecks)
        }
        super.onCleared()
    }
}

interface Config {
    val debitTitle: String
    val creditTitle: String
    val kidTitle: String
    val kinderSurprizeCashback: Long
    val specialUsers: List<Long>
}

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

enum class CardType { DEBIT, CREDIT, KID, PREMIUM }

class UiModel(val title: String, val description: String)
