package ru.alox1d.vknewsapp.ui

import ru.alox1d.vknewsapp.domain.FeedPost

sealed class NewsFeedScreenState {
    data object Initial : NewsFeedScreenState()
    data class Posts(val posts: List<FeedPost>) : NewsFeedScreenState()
}