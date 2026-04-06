package com.tasktracker.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String,
    val description: String,
    val isCompleted: Boolean,
    val createdAt: String
)