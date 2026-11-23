
package com.rparchitect.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "characters",
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
data class RPCharacter(
    @PrimaryKey
    val id: Long,
    val projectId: Long,
    val name: String,
    val role: String,
    val age: String,
    val gender: String,
    val occupation: String,
    val personality: String,
    val background: String,
    val appearance: String,
    val relationship: String,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)
