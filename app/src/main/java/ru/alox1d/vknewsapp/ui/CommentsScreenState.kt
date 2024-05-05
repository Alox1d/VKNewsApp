package ru.alox1d.vknewsapp.ui

import ru.alox1d.vknewsapp.domain.FeedPost
import ru.alox1d.vknewsapp.domain.PostComment

sealed class CommentsScreenState {
    data object Initial : CommentsScreenState()
    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}