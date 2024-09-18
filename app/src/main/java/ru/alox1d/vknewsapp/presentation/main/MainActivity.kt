package ru.alox1d.vknewsapp.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.alox1d.vknewsapp.domain.entity.AuthState
import ru.alox1d.vknewsapp.presentation.NewsFeedApplication
import ru.alox1d.vknewsapp.presentation.ViewModelFactory
import ru.alox1d.vknewsapp.presentation.ui.theme.VKNewsAppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as NewsFeedApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            VKNewsAppTheme {
                val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
                val authState = viewModel.authState.collectAsState(AuthState.Initial)

                when (authState.value) {
                    AuthState.Authorized -> MainScreen(viewModelFactory)
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