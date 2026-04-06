package com.tasktracker.repository

import com.tasktracker.model.Task
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class TaskRepository {
    private val fileName = "tasks.json"
    private val json = Json { ignoreUnknownKeys = true }

    fun save(tasks: List<Task>) {
        val jsonContent = json.encodeToString(tasks)
        File(fileName).writeText(jsonContent)
    }

    fun load(): List<Task> {
        return try {
            val file = File(fileName)
            if (file.exists()) {
                val content = file.readText()
                if (content.isNotBlank()) {
                    json.decodeFromString(content)
                } else {
                    emptyList()
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}