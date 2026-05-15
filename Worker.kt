package com.nammamistri.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workers",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class Worker(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val wage: Double,
    val advance: Double,
    val projectId: Long
)
