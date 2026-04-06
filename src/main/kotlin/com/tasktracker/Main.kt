package com.tasktracker

import com.tasktracker.service.TaskService
import com.tasktracker.repository.TaskRepository
import com.tasktracker.util.CommandParser

fun main(args: Array<String>) {
    val repository = TaskRepository()
    val service = TaskService(repository)
    val command = CommandParser.parse(args)

    when (command) {
        is CommandParser.Command.Add -> {
            val task = service.addTask(command.description)
            println("Added task: ${task.id} - ${task.description}")
        }
        is CommandParser.Command.List -> {
            val tasks = service.listTasks(done = command.done, open = command.open)
            if (tasks.isEmpty()) {
                println("No tasks found.")
            } else {
                println("Tasks:")
                tasks.forEach { task ->
                    val status = if (task.isCompleted) "[X]" else "[ ]"
                    if (task.isCompleted) {
                        // For completed tasks, make the entire row green
                        val formattedRow = "\u001b[32m$status ${task.id} - ${task.description}\u001b[0m"
                        println("  $formattedRow")
                    } else {
                        val formattedStatus = status
                        println("  $formattedStatus ${task.id} - ${task.description}")
                    }
                }
            }
        }
        is CommandParser.Command.Done -> {
            val success = service.completeTask(command.id)
            if (success) {
                println("Task completed.")
            } else {
                println("Task not found.")
            }
        }
        is CommandParser.Command.Remove -> {
            val success = service.deleteTask(command.id)
            if (success) {
                println("Task removed.")
            } else {
                println("Task not found.")
            }
        }
        is CommandParser.Command.Help -> {
            println("Task Tracker CLI")
            println("Usage:")
            println("  add \"<task description>\"   - Add a new task")
            println("  list                       - List all tasks")
            println("  list --done                - List completed tasks only")
            println("  list --open                - List open tasks only")
            println("  list --done --open         - List all tasks (default)")
            println("  done <task_id>             - Mark task as completed")
            println("  remove <task_id>           - Remove a task")
            println("  search <text>              - Search tasks by description")
            println("  help                       - Show this help")
        }
        is CommandParser.Command.Error -> {
            println("Error: ${command.message}")
        }
        is CommandParser.Command.Search -> {
            val tasks = service.searchTasks(command.text)
            if (tasks.isEmpty()) {
                println("No tasks found matching \"$${command.text}\".")
            } else {
                println("Search results for \"$${command.text}\":")
                tasks.forEach { task ->
                    val status = if (task.isCompleted) "[X]" else "[ ]"
                    if (task.isCompleted) {
                        // For completed tasks, make the entire row green
                        val formattedRow = "\u001b[32m$status ${task.id} - ${task.description}\u001b[0m"
                        println("  $formattedRow")
                    } else {
                        val formattedStatus = status
                        println("  $formattedStatus ${task.id} - ${task.description}")
                    }
                }
            }
        }
    }
}