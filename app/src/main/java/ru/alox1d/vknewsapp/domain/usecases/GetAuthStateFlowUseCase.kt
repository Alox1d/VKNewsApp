package ru.alox1d.vknewsapp.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import ru.alox1d.vknewsapp.domain.entity.AuthState
import ru.alox1d.vknewsapp.domain.repository.NewsFeedRepository
import javax.inject.Inject

class GetAuthStateFlowUseCase @Inject constructor(
    private val repository: NewsFeedRepository,
) {

    fun invoke(): StateFlow<AuthState> {
        return repository.authStateFlow
    }
}