package ru.alox1d.vknewsapp.data.mapper

import ru.alox1d.vknewsapp.data.model.NewsFeedResponseDto
import ru.alox1d.vknewsapp.data.model.newsfeed.comments.CommentsResponseDto
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.domain.entity.PostComment
import ru.alox1d.vknewsapp.domain.entity.StatisticItem
import ru.alox1d.vknewsapp.domain.entity.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue

class NewsFeedMapper @Inject constructor() {

    fun mapDtoToDomain(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()
        val posts = responseDto.newsFeedContentDto.posts
        val groups = responseDto.newsFeedContentDto.groups

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: break
            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId,
                communityName = group.name,
                publicationDate = mapTimestampToDate(post.date),
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
                ),
                isLiked = post.likes.userLikes > 0
            )
            result.add(feedPost)
        }

        return result
    }

    fun mapResponseToComments(response: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()
        val comments = response.content.comments
        val profiles = response.content.profiles
        for (comment in comments) {
            if (comment.text.isBlank()) continue

            val author = profiles.firstOrNull { it.id == comment.authorId } ?: continue
            val postComment = PostComment(
                id = comment.id,
                authorName = "${author.firstName} ${author.lastName}",
                authorAvatarUrl = author.avatarUrl,
                commentText = comment.text,
                publicationDate = mapTimestampToDate(comment.date)
            )
            result.add(postComment)
        }
        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val timestamp = timestamp * 1000
        val date = Date(timestamp)
        return SimpleDateFormat(
            "d MMMM yyyy, hh:mm",
            Locale.getDefault()
        ).format(date)
    }
}