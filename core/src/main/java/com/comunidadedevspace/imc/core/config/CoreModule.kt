package com.comunidadedevspace.imc.core.config

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.comunidadedevspace.imc.core.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideDispatcherProvider(dispatcher: CoroutineDispatcher): DispatcherProvider = DispatcherProvider.Default(dispatcher)
}
