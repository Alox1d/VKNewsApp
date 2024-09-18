package ru.alox1d.vknewsapp.presentation

import android.app.Application
import ru.alox1d.vknewsapp.di.ApplicationComponent
import ru.alox1d.vknewsapp.di.DaggerApplicationComponent
import ru.alox1d.vknewsapp.domain.entity.FeedPost

class NewsFeedApplication : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(
            this, FeedPost(
                id = 8552,
                communityId = 7730,
                communityName = "Branden Tran",
                publicationDate = "invenire",
                communityImageUrl = "https://search.yahoo.com/search?p=postea",
                contextText = "invenire",
                contentImageUrl = null,
                statistics = listOf(),
                isLiked = false
            )
        )
    }
}