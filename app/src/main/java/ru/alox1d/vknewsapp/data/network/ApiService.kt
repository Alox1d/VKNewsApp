package ru.alox1d.vknewsapp.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.alox1d.vknewsapp.data.model.NewsFeedResponseDto

interface ApiService {

    @GET("newsfeed.getRecommended?v=5.199")
    suspend fun loadRecommendations(
        @Query("access_token") token: String
    ): NewsFeedResponseDto
}