package ru.alox1d.vknewsapp.ui

import ru.alox1d.vknewsapp.domain.FeedPost
import ru.alox1d.vknewsapp.domain.PostComment

sealed class HomeScreenState {
    data object Initial : HomeScreenState()
    data class Posts(val posts: List<FeedPost>) : HomeScreenState()
    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : HomeScreenState()
}