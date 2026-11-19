
package com.rparchitect.app.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "world_lore",
    indices = [Index(value = ["projectId"])]
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
