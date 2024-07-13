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

    private var nextFrom: String? = null

    suspend fun loadRecommendations(): List<FeedPost> {
        val startFrom = nextFrom

        if (startFrom == null && feedPosts.isNotEmpty()) return feedPosts

        val response = if (startFrom == null) {
            apiService.loadRecommendations(getAccessToken())
        } else {
            apiService.loadRecommendations(
                token = getAccessToken(),
                startFrom = startFrom
            )
        }
        nextFrom = response.newsFeedContentDto.nextFrom
        val posts = mapper.mapDtoToDomain(response)
        _feedPosts.addAll(posts)

        return feedPosts
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

    suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
        )
        _feedPosts.remove(feedPost)
    }

    private suspend fun getAccessToken(): String {
        return dataStore.data.firstOrNull()
            ?.get(DataStore.prefsAccessTokenKey) ?: throw IllegalStateException("AT is null")
    }

}