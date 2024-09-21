package ru.alox1d.vknewsapp.presentation

import android.app.Application
import ru.alox1d.vknewsapp.di.ApplicationComponent
import ru.alox1d.vknewsapp.di.DaggerApplicationComponent

class NewsFeedApplication : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}