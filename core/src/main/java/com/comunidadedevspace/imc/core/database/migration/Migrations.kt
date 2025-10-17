package com.comunidadedevspace.imc.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 =
    object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS sync_logs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    recordId INTEGER NOT NULL,
                    status TEXT NOT NULL,
                    message TEXT,
                    timestamp INTEGER NOT NULL
                )
                """.trimIndent(),
            )
            db.execSQL("ALTER TABLE imc_records ADD COLUMN synced INTEGER NOT NULL DEFAULT 0")
        }
    }
