package ru.alox1d.vknewsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import ru.alox1d.vknewsapp.ui.MainScreen
import ru.alox1d.vknewsapp.ui.theme.VKNewsAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VKNewsAppTheme {
                MainScreen(viewModel)
            }
        }
    }
}