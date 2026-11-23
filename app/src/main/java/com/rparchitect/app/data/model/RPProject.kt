
package com.rparchitect.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class RPProject(
    @PrimaryKey
    val id: Long,
    val name: String,
    val description: String,
    val genre: String,
    val createdAt: Long,
    val updatedAt: Long
)
