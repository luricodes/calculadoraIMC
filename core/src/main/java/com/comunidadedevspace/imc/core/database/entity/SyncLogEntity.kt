package com.comunidadedevspace.imc.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "sync_logs")
data class SyncLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordId: Long,
    val status: String,
    val message: String?,
    val timestamp: Instant = Instant.now(),
)
