package com.tasktracker.repository

import com.tasktracker.model.Task
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class TaskRepositoryTest {

    private lateinit var repository: TaskRepository
    private lateinit var tempFile: File

    @BeforeEach
    fun setUp() {
        // Create a temporary file for testing
        tempFile = File.createTempFile("tasks", ".json")
        tempFile.deleteOnExit()
        
        // Create repository with temp file path - but since current implementation uses fixed fileName,
        // we'll test with a temporary file in the working directory
        repository = TaskRepository()
    }

    @AfterEach
    fun tearDown() {
        // Clean up temp file if it exists
        if (tempFile.exists()) {
            tempFile.delete()
        }
        // Clean up the test file if it exists
        val testFile = File("tasks.json")
        if (testFile.exists()) {
            testFile.delete()
        }
    }

    @Test
    fun `test save and load empty list`() {
        // Test saving empty list
        val emptyList = mutableListOf<Task>()
        repository.save(emptyList)
        
        // Test loading empty list
        val loadedTasks = repository.load()
        assertTrue(loadedTasks.isEmpty())
    }

    @Test
    fun `test save and load single task`() {
        val task = Task("1", "Test task", false, "2023-01-01")
        val taskList = mutableListOf(task)
        
        // Save task
        repository.save(taskList)
        
        // Load task
        val loadedTasks = repository.load()
        
        // Verify
        assertEquals(1, loadedTasks.size)
        assertEquals(task.id, loadedTasks[0].id)
        assertEquals(task.description, loadedTasks[0].description)
        assertEquals(task.isCompleted, loadedTasks[0].isCompleted)
        assertEquals(task.createdAt, loadedTasks[0].createdAt)
    }

    @Test
    fun `test save and load multiple tasks`() {
        val tasks = mutableListOf(
            Task("1", "First task", false, "2023-01-01"),
            Task("2", "Second task", true, "2023-01-02"),
            Task("3", "Third task", false, "2023-01-03")
        )
        
        // Save multiple tasks
        repository.save(tasks)
        
        // Load tasks
        val loadedTasks = repository.load()
        
        // Verify
        assertEquals(3, loadedTasks.size)
        assertEquals(tasks[0].id, loadedTasks[0].id)
        assertEquals(tasks[0].description, loadedTasks[0].description)
        assertEquals(tasks[0].isCompleted, loadedTasks[0].isCompleted)
        assertEquals(tasks[0].createdAt, loadedTasks[0].createdAt)
        
        assertEquals(tasks[1].id, loadedTasks[1].id)
        assertEquals(tasks[1].description, loadedTasks[1].description)
        assertEquals(tasks[1].isCompleted, loadedTasks[1].isCompleted)
        assertEquals(tasks[1].createdAt, loadedTasks[1].createdAt)
        
        assertEquals(tasks[2].id, loadedTasks[2].id)
        assertEquals(tasks[2].description, loadedTasks[2].description)
        assertEquals(tasks[2].isCompleted, loadedTasks[2].isCompleted)
        assertEquals(tasks[2].createdAt, loadedTasks[2].createdAt)
    }

    @Test
    fun `test load from empty file`() {
        // Create an empty file
        File("tasks.json").writeText("")
        
        // Load from empty file
        val loadedTasks = repository.load()
        
        // Should return empty list
        assertTrue(loadedTasks.isEmpty())
    }

    @Test
    fun `test load from invalid JSON`() {
        // Write invalid JSON to file
        File("tasks.json").writeText("{invalid json}")
        
        // Load should not throw exception and return empty list
        val loadedTasks = repository.load()
        assertTrue(loadedTasks.isEmpty())
    }

    @Test
    fun `test load from corrupted JSON`() {
        // Write partially valid JSON to file
        File("tasks.json").writeText("[{\"id\":\"1\", \"description\":\"Task\"}")
        
        // Load should not throw exception and return empty list
        val loadedTasks = repository.load()
        assertTrue(loadedTasks.isEmpty())
    }

    @Test
    fun `test load from non-existent file`() {
        // Delete the tasks.json file to simulate non-existent file
        val testFile = File("tasks.json")
        if (testFile.exists()) {
            testFile.delete()
        }
        
        // Load should not throw exception and return empty list
        val loadedTasks = repository.load()
        assertTrue(loadedTasks.isEmpty())
    }

    @Test
    fun `test save to existing file`() {
        // Create initial content
        val initialTask = Task("1", "Initial task", false, "2023-01-01")
        val initialTasks = mutableListOf(initialTask)
        
        // Save initial tasks
        repository.save(initialTasks)
        
        // Load and verify
        val loadedTasks = repository.load()
        assertEquals(1, loadedTasks.size)
        assertEquals("1", loadedTasks[0].id)
        
        // Add new task and save
        val newTask = Task("2", "New task", true, "2023-01-02")
        val updatedTasks = mutableListOf(newTask)
        repository.save(updatedTasks)
        
        // Load and verify updated content
        val updatedLoadedTasks = repository.load()
        assertEquals(1, updatedLoadedTasks.size)
        assertEquals("2", updatedLoadedTasks[0].id)
        assertEquals("New task", updatedLoadedTasks[0].description)
        assertEquals(true, updatedLoadedTasks[0].isCompleted)
    }

    @Test
    fun `test load and save with special characters`() {
        val specialTask = Task("1", "Task with \"quotes\" and \n newlines", false, "2023-01-01")
        val tasks = mutableListOf(specialTask)
        
        repository.save(tasks)
        val loadedTasks = repository.load()
        
        assertEquals(1, loadedTasks.size)
        assertEquals(specialTask.description, loadedTasks[0].description)
    }

    @Test
    fun `test load large number of tasks`() {
        val tasks = mutableListOf<Task>()
        for (i in 1..100) {
            tasks.add(Task("$i", "Task $i", i % 2 == 0, "2023-01-01"))
        }
        
        repository.save(tasks)
        val loadedTasks = repository.load()
        
        assertEquals(100, loadedTasks.size)
    }
}