package ru.alox1d.vknewsapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.alox1d.vknewsapp.data.network.ApiFactory
import ru.alox1d.vknewsapp.data.network.ApiService
import ru.alox1d.vknewsapp.data.repository.NewsFeedRepositoryImpl
import ru.alox1d.vknewsapp.domain.repository.NewsFeedRepository
import ru.alox1d.vknewsapp.presentation.main.appDataStore

private const val USER_PREFERENCES = "user_preferences"

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun provideDataStore(
            context: Context
        ): DataStore<Preferences> {
            return context.appDataStore
        }

//        @Singleton
//        @Provides
//        fun providePreferencesDataStore(appContext: Context): DataStore<Preferences> {
//            return PreferenceDataStoreFactory.create(
//                corruptionHandler = ReplaceFileCorruptionHandler(
//                    produceNewData = { emptyPreferences() }
//                ),
//                migrations = listOf(SharedPreferencesMigration(appContext,USER_PREFERENCES)),
//                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
//                produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
//            )
//        }
    }
}