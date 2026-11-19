package com.rparchitect.app.data.model

data class RPCharacter(
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
