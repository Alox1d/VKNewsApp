package ru.alox1d.vknewsapp.data.model

import com.google.gson.annotations.SerializedName
import ru.alox1d.vknewsapp.data.model.newsfeed.GroupDto
import ru.alox1d.vknewsapp.data.model.newsfeed.PostDto

data class NewsFeedContentDto(
    @SerializedName("items") val posts: List<PostDto>,
    @SerializedName("groups") val groups: List<GroupDto>,
)
