package com.rparchitect.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "world_lore",
    foreignKeys = [
        ForeignKey(
            entity = RPProject::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["projectId"])
    ]
)
data class RPWorldLore(
    @PrimaryKey
    val id: Long,
    val projectId: Long,
    val title: String,
    val history: String,
    val culture: String,
    val worldRules: String,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)
