package com.nammamistri.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PhotoDao {
    @Insert
    suspend fun insert(photo: Photo): Long

    @Update
    suspend fun update(photo: Photo)

    @Delete
    suspend fun delete(photo: Photo)

    @Query("SELECT * FROM photos WHERE projectId = :projectId ORDER BY id DESC")
    suspend fun getByProject(projectId: Long): List<Photo>
}
