
package com.rparchitect.app.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "characters",
    indices = [Index(value = ["projectId"])]
)
data class RPCharacter(
    @PrimaryKey
    val id: Long,
    val projectId: Long,
    val name: String,
    val role: String,
    val age: String,
    val gender: String,
    val personality: String,
    val background: String,
    val appearance: String,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)
