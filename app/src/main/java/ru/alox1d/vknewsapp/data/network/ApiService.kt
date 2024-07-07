package ru.alox1d.vknewsapp.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.alox1d.vknewsapp.data.model.NewsFeedResponseDto
import ru.alox1d.vknewsapp.data.model.newsfeed.likes.LikesCountResponseDto

interface ApiService {

    @GET("newsfeed.getRecommended?v=$API_VERSION")
    suspend fun loadRecommendations(
        @Query("access_token") token: String
    ): NewsFeedResponseDto

    @GET("likes.add?v=$API_VERSION&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    ): LikesCountResponseDto

    @GET("likes.delete?v=$API_VERSION&type=post")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    ): LikesCountResponseDto

    private companion object {

        const val API_VERSION = "5.199"
    }
}