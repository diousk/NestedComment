package com.lang.commentsystem.epoxy.repo

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommentModule {
    @Binds
    @Singleton
    abstract fun provideCommentRepo(impl: CommentRepoImpl): CommentRepo
}