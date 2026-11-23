package com.rparchitect.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "custom_template_options",
    foreignKeys = [
        ForeignKey(
            entity = RPCustomTemplateField::class,
            parentColumns = ["id"],
            childColumns = ["fieldId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["fieldId"])
    ]
)
data class RPCustomTemplateOption(
    @PrimaryKey
    val id: String,
    val fieldId: String,
    val value: String,
    val sortOrder: Int = 0
)
