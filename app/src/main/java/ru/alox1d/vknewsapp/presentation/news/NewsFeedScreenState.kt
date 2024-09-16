package ru.alox1d.vknewsapp.presentation.news

import ru.alox1d.vknewsapp.domain.entity.FeedPost

sealed class NewsFeedScreenState {
    data object Initial : NewsFeedScreenState()
    data object Loading : NewsFeedScreenState()
    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : NewsFeedScreenState()
}