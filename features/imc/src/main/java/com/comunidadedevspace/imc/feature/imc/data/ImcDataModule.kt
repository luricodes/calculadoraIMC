package com.comunidadedevspace.imc.feature.imc.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImcDataModule {
    @Binds
    @Singleton
    abstract fun bindImcRepository(impl: ImcRepositoryImpl): ImcRepository
}
