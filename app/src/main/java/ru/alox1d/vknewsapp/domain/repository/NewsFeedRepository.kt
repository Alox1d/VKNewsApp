package ru.alox1d.vknewsapp.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.alox1d.vknewsapp.domain.entity.AuthState
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.domain.entity.NewsFeedResult
import ru.alox1d.vknewsapp.domain.entity.PostComment

interface NewsFeedRepository {
    val authStateFlow: StateFlow<AuthState>
    val recommendations: StateFlow<NewsFeedResult>
    fun getComments(feedPost: FeedPost): Flow<List<PostComment>>

    suspend fun loadNextData()
    suspend fun changeLikeStatus(feedPost: FeedPost)
    suspend fun deletePost(feedPost: FeedPost)

    suspend fun getAccessToken(): String?
    suspend fun updateAuthState(token: String?)
}