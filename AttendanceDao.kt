package com.nammamistri.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attendance: Attendance): Long

    @Upsert
    suspend fun upsert(attendance: Attendance)

    @Update
    suspend fun update(attendance: Attendance)

    @Delete
    suspend fun delete(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE projectId = :projectId ORDER BY workerId")
    suspend fun getByProject(projectId: Long): List<Attendance>

    @Query("SELECT * FROM attendance WHERE workerId = :workerId AND projectId = :projectId LIMIT 1")
    suspend fun getByWorker(workerId: Long, projectId: Long): Attendance?

    @Query(
        """
        INSERT OR REPLACE INTO attendance(id, workerId, projectId, daysWorked)
        VALUES(
            COALESCE((SELECT id FROM attendance WHERE workerId = :workerId AND projectId = :projectId), 0),
            :workerId,
            :projectId,
            :daysWorked
        )
        """
    )
    suspend fun insertOrUpdate(workerId: Long, projectId: Long, daysWorked: Int)
}
