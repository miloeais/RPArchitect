package com.rparchitect.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "custom_template_fields",
    foreignKeys = [
        ForeignKey(
            entity = RPCustomTemplate::class,
            parentColumns = ["id"],
            childColumns = ["templateId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["templateId"])
    ]
)
data class RPCustomTemplateField(
    @PrimaryKey
    val id: String,
    val templateId: String,
    val label: String,
    val type: String,
    val required: Boolean = false
)
