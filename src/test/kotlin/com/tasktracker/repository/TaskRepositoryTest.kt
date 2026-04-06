package com.tasktracker.repository

import com.tasktracker.model.Task
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import java.io.File

class TaskRepositoryTest {

    private lateinit var repository: TaskRepository

    @BeforeEach
    fun setUp() {
        // Create repository with fresh instance (uses fixed file name)
        repository = TaskRepository()
    }

    @AfterEach
    fun tearDown() {
        // Clean up test file if it exists
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
        // Test saving single task
        val task = Task("1", "Test task", false, "2023-01-01")
        repository.save(listOf(task))
        
        // Test loading single task
        val loadedTasks = repository.load()
        assertEquals(1, loadedTasks.size)
        assertEquals(task.id, loadedTasks[0].id)
        assertEquals(task.description, loadedTasks[0].description)
        assertEquals(task.isCompleted, loadedTasks[0].isCompleted)
        assertEquals(task.createdAt, loadedTasks[0].createdAt)
    }

    @Test
    fun `test save and load multiple tasks`() {
        // Test saving multiple tasks
        val tasks = listOf(
            Task("1", "Task 1", false, "2023-01-01"),
            Task("2", "Task 2", true, "2023-01-02"),
            Task("3", "Task 3", false, "2023-01-03")
        )
        repository.save(tasks)
        
        // Test loading multiple tasks
        val loadedTasks = repository.load()
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
        val testFile = File("tasks.json")
        testFile.writeText("")
        
        // Load from empty file
        val loadedTasks = repository.load()
        assertTrue(loadedTasks.isEmpty())
    }

    @Test
    fun `test load from invalid JSON`() {
        // Create file with invalid JSON
        val testFile = File("tasks.json")
        testFile.writeText("{invalid json}")
        
        // Load from invalid JSON - should return empty list
        val loadedTasks = repository.load()
        assertTrue(loadedTasks.isEmpty())
    }

    @Test
    fun `test load from corrupted JSON`() {
        // Create file with corrupted JSON
        val testFile = File("tasks.json")
        testFile.writeText("[{invalid json}]")
        
        // Load from corrupted JSON - should return empty list
        val loadedTasks = repository.load()
        assertTrue(loadedTasks.isEmpty())
    }

    @Test
    fun `test load from non-existent file`() {
        // Delete the file to make sure it's non-existent
        val testFile = File("tasks.json")
        if (testFile.exists()) {
            testFile.delete()
        }
        
        // Load from non-existent file - should return empty list
        val loadedTasks = repository.load()
        assertTrue(loadedTasks.isEmpty())
    }

    @Test
    fun `test save and overwrite functionality`() {
        // Save first set of tasks
        val tasks1 = listOf(Task("1", "Task 1", false, "2023-01-01"))
        repository.save(tasks1)
        val loadedTasks1 = repository.load()
        assertEquals(1, loadedTasks1.size)
        
        // Save second set of tasks
        val tasks2 = listOf(Task("2", "Task 2", true, "2023-01-02"))
        repository.save(tasks2)
        val loadedTasks2 = repository.load()
        assertEquals(1, loadedTasks2.size)
        assertEquals("2", loadedTasks2[0].id)
    }

    @Test
    fun `test save and load task with special characters`() {
        // Test with special characters in description
        val task = Task("1", "Task with \"quotes\" and \n newlines", false, "2023-01-01")
        repository.save(listOf(task))
        
        val loadedTasks = repository.load()
        assertEquals(1, loadedTasks.size)
        assertEquals(task.description, loadedTasks[0].description)
    }

    @Test
    fun `test save and load large number of tasks`() {
        // Create and save many tasks
        val tasks = (1..100).map { 
            Task(it.toString(), "Task $it", it % 2 == 0, "2023-01-01") 
        }
        repository.save(tasks)
        
        val loadedTasks = repository.load()
        assertEquals(100, loadedTasks.size)
        
        // Verify first and last task
        assertEquals("1", loadedTasks[0].id)
        assertEquals("100", loadedTasks[99].id)
    }
}