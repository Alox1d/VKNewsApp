package ru.alox1d.vknewsapp.presentation.comments

import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.domain.entity.PostComment

sealed class CommentsScreenState {
    data object Initial : CommentsScreenState()
    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}