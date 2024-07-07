package ru.alox1d.vknewsapp.data.model

import com.google.gson.annotations.SerializedName

data class NewsFeedResponseDto(
    @SerializedName("response") val newsFeedContentDto: NewsFeedContentDto,
)
