package ru.alox1d.vknewsapp.domain

import ru.alox1d.vknewsapp.R

data class PostComment(
    val id: Int,
    val authorName: String = "Author",
    val authorAvatarResId: Int = R.drawable.comment_author_avatar,
    val commentText: String = "Long comment text",
    val publicationDate: String = "14:00",
)
