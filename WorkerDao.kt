package com.nammamistri.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkerDao {
    @Insert
    suspend fun insert(worker: Worker): Long

    @Update
    suspend fun update(worker: Worker)

    @Delete
    suspend fun delete(worker: Worker)

    @Query("SELECT * FROM workers WHERE projectId = :projectId ORDER BY name COLLATE NOCASE")
    suspend fun getByProject(projectId: Long): List<Worker>

    @Query("SELECT COUNT(*) FROM workers WHERE projectId = :projectId")
    suspend fun countByProject(projectId: Long): Int
}
