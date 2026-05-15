package com.nammamistri.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProjectDao {
    @Insert
    suspend fun insert(project: Project): Long

    @Update
    suspend fun update(project: Project)

    @Delete
    suspend fun delete(project: Project)

    @Query("SELECT * FROM projects ORDER BY createdDate DESC")
    suspend fun getAll(): List<Project>

    @Query("SELECT * FROM projects WHERE id = :projectId LIMIT 1")
    suspend fun getById(projectId: Long): Project?

    @Query("SELECT * FROM projects WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' ORDER BY createdDate DESC")
    suspend fun searchByName(query: String): List<Project>
}
