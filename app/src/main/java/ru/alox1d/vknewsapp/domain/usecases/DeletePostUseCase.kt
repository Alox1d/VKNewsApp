package ru.alox1d.vknewsapp.domain.usecases

import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.domain.repository.NewsFeedRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: NewsFeedRepository,
) {

    suspend fun invoke(feedPost: FeedPost) {
        return repository.deletePost(feedPost)
    }
}