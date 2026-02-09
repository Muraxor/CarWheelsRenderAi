package car.wheels.renderai.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface TestRepository {
    val myModels: Flow<List<String>>
}

class DefaultTestRepository @Inject constructor() : TestRepository {

    override val myModels: Flow<List<String>> =
        flowOf(listOf("Test", "NotTest"))
}