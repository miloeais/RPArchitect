package com.rparchitect.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "world_entries",
    foreignKeys = [
        ForeignKey(
            entity = RPProject::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RPWorldEntry::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["projectId"]),
        Index(value = ["parentId"])
    ]
)
data class RPWorldEntry(
    @PrimaryKey
    val id: Long,
    val projectId: Long,
    val parentId: Long?,
    val name: String,
    val type: String,
    val description: String,
    val history: String,
    val government: String,
    val leader: String,
    val headquarters: String,
    val territory: String,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)
