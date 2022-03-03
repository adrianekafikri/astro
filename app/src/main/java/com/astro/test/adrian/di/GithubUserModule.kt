package com.astro.test.adrian.di

import com.astro.test.adrian.domain.GithubUserInteractor
import com.astro.test.adrian.domain.GithubUserInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by adrianekafikri on 03/03/22.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class GithubUserModule {

    @Binds
    abstract fun bindGithubUserInteractor(interactor: GithubUserInteractorImpl): GithubUserInteractor
}