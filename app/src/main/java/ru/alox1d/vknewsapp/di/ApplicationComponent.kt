package ru.alox1d.vknewsapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.alox1d.vknewsapp.domain.entity.FeedPost
import ru.alox1d.vknewsapp.presentation.main.MainActivity

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
//        DomainModule::class,
        ViewModelModule::class,
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance feedPost: FeedPost,
        ): ApplicationComponent
    }
}