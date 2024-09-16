package ru.alox1d.vknewsapp.domain.usecases

import ru.alox1d.vknewsapp.domain.repository.NewsFeedRepository

class LoadNextDataUseCase(
    private val repository: NewsFeedRepository,
) {

    suspend fun invoke() {
        return repository.loadNextData()
    }
}