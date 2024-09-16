package ru.alox1d.vknewsapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.domain.entity.PostComment
import ru.alox1d.vknewsapp.domain.repository.NewsFeedRepository

class GetCommentsUseCase(
    private val repository: NewsFeedRepository,
) {

    fun invoke(feedPost: FeedPost): Flow<List<PostComment>> {
        return repository.getComments(feedPost)
    }
}