package ru.alox1d.vknewsapp.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.alox1d.vknewsapp.domain.AuthState
import ru.alox1d.vknewsapp.presentation.ui.theme.VKNewsAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VKNewsAppTheme {
                val viewModel: MainViewModel = viewModel()
                val authState = viewModel.authState.collectAsState(AuthState.Initial)

                when (authState.value) {
                    AuthState.Authorized -> MainScreen()
                    AuthState.NotAuthorized -> LoginScreen(
                        onSuccessAuth = viewModel.onLoginSuccess(),
                        onFailAuth = viewModel.onLoginError()
                    )

                    AuthState.Initial -> Unit
                }
            }
        }
    }
}