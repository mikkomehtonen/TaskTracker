package com.tasktracker.service

import com.tasktracker.model.Task
import com.tasktracker.repository.TaskRepository
import java.util.UUID

class TaskService(private val repository: TaskRepository) {
    private var tasks = repository.load()

    fun addTask(description: String): Task {
        val task = Task(
            id = UUID.randomUUID().toString(),
            description = description,
            isCompleted = false,
            createdAt = java.time.Instant.now().toString()
        )
        tasks = tasks + task
        repository.save(tasks)
        return task
    }

    fun listTasks(done: Boolean = false, open: Boolean = false): List<Task> {
        // If no flags are set, return all tasks
        if (!done && !open) {
            return tasks
        }
        
        // Return only the filtered tasks
        return tasks.filter { task ->
            when {
                done -> task.isCompleted
                open -> !task.isCompleted
                else -> true
            }
        }
    }

    fun completeTask(id: String): Boolean {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            val updatedTask = tasks[index].copy(isCompleted = true)
            tasks = tasks.toMutableList().apply { this[index] = updatedTask }
            repository.save(tasks)
            return true
        }
        return false
    }

    fun deleteTask(id: String): Boolean {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            tasks = tasks.toMutableList().apply { removeAt(index) }
            repository.save(tasks)
            return true
        }
        return false
    }

    fun getTask(id: String): Task? {
        return tasks.find { it.id == id }
    }

    fun searchTasks(text: String): List<Task> {
        return tasks.filter { task ->
            task.description.contains(text, ignoreCase = true)
        }
    }
}