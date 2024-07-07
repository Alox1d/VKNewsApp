package ru.alox1d.vknewsapp.data.mapper

import ru.alox1d.vknewsapp.data.model.NewsFeedResponseDto
import ru.alox1d.vknewsapp.domain.FeedPost
import ru.alox1d.vknewsapp.domain.StatisticItem
import ru.alox1d.vknewsapp.domain.StatisticType
import kotlin.math.absoluteValue

class NewsFeedMapper {

    fun mapDtoToDomain(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()
        val posts = responseDto.newsFeedContentDto.posts
        val groups = responseDto.newsFeedContentDto.groups

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: break
            val feedPost = FeedPost(
                id = post.id,
                communityName = group.name,
                publicationDate = post.date.toString(),
                communityImageUrl = group.imageUrl,
                contextText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(
                        type = StatisticType.LIKES,
                        post.likes.count
                    ),
                    StatisticItem(
                        type = StatisticType.SHARES,
                        post.reposts.count
                    ),
                    StatisticItem(
                        type = StatisticType.VIEWS,
                        post.views.count
                    ),
                    StatisticItem(
                        type = StatisticType.COMMENTS,
                        post.comments.count
                    )
                )
            )
            result.add(feedPost)
        }

        return result
    }
}