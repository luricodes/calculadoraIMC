package com.comunidadedevspace.imc.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.comunidadedevspace.imc.core.database.dao.ImcRecordDao
import com.comunidadedevspace.imc.core.database.entity.AppTypeConverters
import com.comunidadedevspace.imc.core.database.entity.ImcRecordEntity
import com.comunidadedevspace.imc.core.database.entity.SyncLogEntity

@Database(
    entities = [ImcRecordEntity::class, SyncLogEntity::class],
    version = 2,
    exportSchema = true,
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imcRecordDao(): ImcRecordDao
}
