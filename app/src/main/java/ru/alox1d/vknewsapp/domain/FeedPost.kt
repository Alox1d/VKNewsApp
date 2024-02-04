package ru.alox1d.vknewsapp.domain

import ru.alox1d.vknewsapp.R

data class FeedPost(
    val communityName: String = "/dev/null",
    val publicationDate: String = "14:00",
    val avatarResId: Int = R.drawable.post_community_thumbnail,
    val contextText: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    val contentImageResId: Int = R.drawable.post_content_image,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(type = StatisticType.VIEWS, count = 966),
        StatisticItem(type = StatisticType.SHARES, count = 7),
        StatisticItem(type = StatisticType.COMMENTS, count = 8),
        StatisticItem(type = StatisticType.LIKES, count = 27)
    )
)
