package com.rparchitect.app.data.model

data class RPWorldEntry(
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
