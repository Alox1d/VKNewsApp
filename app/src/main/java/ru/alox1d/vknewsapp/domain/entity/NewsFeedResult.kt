package ru.alox1d.vknewsapp.domain.entity

sealed class NewsFeedResult {
    data object Error : NewsFeedResult()
    data class Success(val posts: List<FeedPost>) : NewsFeedResult()
}