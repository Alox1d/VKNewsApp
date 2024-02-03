package ru.alox1d.vknewsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.alox1d.vknewsapp.ui.MainScreen
import ru.alox1d.vknewsapp.ui.theme.VKNewsAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VKNewsAppTheme {
                MainScreen()
            }
        }
    }
}