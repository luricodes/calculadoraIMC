package com.comunidadedevspace.imc.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.comunidadedevspace.imc.core.database.entity.ImcRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImcRecordDao {
    @Query("SELECT * FROM imc_records ORDER BY recordedAt DESC")
    fun observeRecords(): Flow<List<ImcRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: ImcRecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<ImcRecordEntity>)

    @Update
    suspend fun update(record: ImcRecordEntity)

    @Query("UPDATE imc_records SET synced = :synced WHERE id IN (:ids)")
    suspend fun markSynced(
        ids: List<Long>,
        synced: Boolean,
    )

    @Query("DELETE FROM imc_records")
    suspend fun clearAll()

    @Query("SELECT * FROM imc_records WHERE synced = 0")
    suspend fun getPendingSync(): List<ImcRecordEntity>
}
