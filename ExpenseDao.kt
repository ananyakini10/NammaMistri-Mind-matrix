package com.nammamistri.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expenses WHERE projectId = :projectId ORDER BY id DESC")
    suspend fun getByProject(projectId: Long): List<Expense>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE projectId = :projectId")
    suspend fun totalByProject(projectId: Long): Double
}
