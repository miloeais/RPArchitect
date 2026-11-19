
package com.rparchitect.app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rp_documents",
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
data class RPDocument(
    @PrimaryKey
    val id: String,
    val projectId: Long,
    val templateId: String,
    val templateName: String,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(
    tableName = "rp_document_fields",
    foreignKeys = [
        ForeignKey(
            entity = RPDocument::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["documentId"])
    ]
)
data class RPDocumentField(
    @PrimaryKey
    val id: String,
    val documentId: String,
    val fieldId: String,
    val label: String,
    val type: String,
    val value: String = "",
    val required: Boolean = false,
    val sortOrder: Int = 0,
    val options: String = "[]"
)
