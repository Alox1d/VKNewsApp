package ru.alox1d.vknewsapp.domain

sealed class NewsFeedResult {
    data object Error : NewsFeedResult()
    data class Success(val posts: List<FeedPost>) : NewsFeedResult()
}