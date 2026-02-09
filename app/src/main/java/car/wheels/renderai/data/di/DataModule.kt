package car.wheels.renderai.data.di

import car.wheels.renderai.data.DefaultTestRepository
import car.wheels.renderai.data.TestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsTestRepositoryRepository(
        myModelRepository: DefaultTestRepository
    ): TestRepository
}
