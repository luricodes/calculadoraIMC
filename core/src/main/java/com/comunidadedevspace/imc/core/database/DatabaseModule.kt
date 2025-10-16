package com.comunidadedevspace.imc.core.database

import android.content.Context
import androidx.room.Room
import com.comunidadedevspace.imc.core.database.dao.ImcRecordDao
import com.comunidadedevspace.imc.core.database.migration.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "imc.db")
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()

    @Provides
    fun provideImcRecordDao(database: AppDatabase): ImcRecordDao = database.imcRecordDao()
}
