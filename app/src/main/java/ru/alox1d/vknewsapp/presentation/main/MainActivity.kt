package ru.alox1d.vknewsapp.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.alox1d.vknewsapp.domain.entity.AuthState
import ru.alox1d.vknewsapp.presentation.getApplicationComponent
import ru.alox1d.vknewsapp.presentation.ui.theme.VKNewsAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val component = getApplicationComponent()
            val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
            val authState = viewModel.authState.collectAsState(AuthState.Initial)

            VKNewsAppTheme {
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