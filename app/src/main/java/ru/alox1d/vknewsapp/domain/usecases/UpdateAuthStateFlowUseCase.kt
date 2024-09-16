package ru.alox1d.vknewsapp.domain.usecases

import ru.alox1d.vknewsapp.domain.repository.NewsFeedRepository

class UpdateAuthStateFlowUseCase(
    private val repository: NewsFeedRepository,
) {

    suspend fun invoke(accessToken: String?) {
        return repository.updateAuthState(token = accessToken)
    }
}