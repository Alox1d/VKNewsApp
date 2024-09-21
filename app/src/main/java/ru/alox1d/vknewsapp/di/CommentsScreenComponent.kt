package ru.alox1d.vknewsapp.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.presentation.ViewModelFactory

@Subcomponent(
    modules = [
        CommentsViewModelModule::class
    ]
)
interface CommentsScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance feedPost: FeedPost,
        ): CommentsScreenComponent
    }
}