package com.rparchitect.app.data.model

data class RPWorldLore(
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
