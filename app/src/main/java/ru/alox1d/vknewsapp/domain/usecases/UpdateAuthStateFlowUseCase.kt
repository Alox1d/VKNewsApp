package ru.alox1d.vknewsapp.domain.usecases

import ru.alox1d.vknewsapp.domain.repository.NewsFeedRepository
import javax.inject.Inject

class UpdateAuthStateFlowUseCase @Inject constructor(
    private val repository: NewsFeedRepository,
) {

    suspend fun invoke(accessToken: String?) {
        return repository.updateAuthState(token = accessToken)
    }
}