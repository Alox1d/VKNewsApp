package ru.alox1d.vknewsapp.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import ru.alox1d.vknewsapp.domain.entity.NewsFeedResult
import ru.alox1d.vknewsapp.domain.repository.NewsFeedRepository

class GetRecommendationsUseCase(
    private val repository: NewsFeedRepository,
) {

    fun invoke(): StateFlow<NewsFeedResult> {
        return repository.recommendations
    }
}