package ru.alox1d.vknewsapp.presentation.main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.appDataStore: DataStore<Preferences> by preferencesDataStore("app")

object DataStore {

    private const val KEY = "key"
    val preferencesKey = stringPreferencesKey(KEY)
}