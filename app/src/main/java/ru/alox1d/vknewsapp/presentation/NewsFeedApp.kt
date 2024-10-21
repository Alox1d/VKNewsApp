package ru.alox1d.vknewsapp.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ru.alox1d.vknewsapp.di.ApplicationComponent
import ru.alox1d.vknewsapp.di.DaggerApplicationComponent

class NewsFeedApplication : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    Log.d("RECOMPOSITION_TAG", "getApplicationComponent: ")
    return (LocalContext.current.applicationContext as NewsFeedApplication).component
}