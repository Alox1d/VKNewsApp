package ru.alox1d.vknewsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.alox1d.vknewsapp.ui.LoginScreen
import ru.alox1d.vknewsapp.ui.MainScreen
import ru.alox1d.vknewsapp.ui.MainVIewModel
import ru.alox1d.vknewsapp.ui.theme.VKNewsAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VKNewsAppTheme {
                val viewModel: MainVIewModel = viewModel()
                val authState = viewModel.authState.observeAsState(AuthState.Initial)

                when (authState.value) {
                    AuthState.Authorized -> MainScreen()
                    AuthState.NotAuthorized -> LoginScreen(
                        onSuccessAuth = viewModel.authSuccessCallback(),
                        onFailAuth = viewModel.authFailCallback()
                    )

                    AuthState.Initial -> Unit
                }
            }
        }
    }
}