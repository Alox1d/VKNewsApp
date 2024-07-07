package ru.alox1d.vknewsapp.data.repository

import android.app.Application
import kotlinx.coroutines.flow.firstOrNull
import ru.alox1d.vknewsapp.data.mapper.NewsFeedMapper
import ru.alox1d.vknewsapp.data.network.ApiFactory
import ru.alox1d.vknewsapp.domain.FeedPost
import ru.alox1d.vknewsapp.domain.StatisticItem
import ru.alox1d.vknewsapp.domain.StatisticType
import ru.alox1d.vknewsapp.presentation.main.DataStore
import ru.alox1d.vknewsapp.presentation.main.appDataStore

class NewsFeedRepository(application: Application) {

    private val dataStore = application.applicationContext.appDataStore
    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    suspend fun loadRecommendations(): List<FeedPost> {
        val response = apiService.loadRecommendations(getAccessToken())
        val posts = mapper.mapDtoToDomain(response)
        _feedPosts.addAll(posts)

        return posts
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count
        val newStats = feedPost.statistics.toMutableList()
            .apply {
                removeIf { it.type == StatisticType.LIKES }
                add(
                    StatisticItem(
                        type = StatisticType.LIKES,
                        count = newLikesCount
                    )
                )
            }
        val newPost = feedPost.copy(
            statistics = newStats,
            isLiked = !feedPost.isLiked
        )
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }

    private suspend fun getAccessToken(): String {
        return dataStore.data.firstOrNull()
            ?.get(DataStore.prefsAccessTokenKey) ?: throw IllegalStateException("AT is null")
    }

}