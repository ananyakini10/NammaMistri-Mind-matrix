package com.nammamistri.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attendance",
    foreignKeys = [
        ForeignKey(
            entity = Worker::class,
            parentColumns = ["id"],
            childColumns = ["workerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workerId"), Index("projectId"), Index(value = ["workerId", "projectId"], unique = true)]
)
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workerId: Long,
    val projectId: Long,
    val daysWorked: Int
)
