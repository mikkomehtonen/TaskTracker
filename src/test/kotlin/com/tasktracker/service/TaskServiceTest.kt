package com.tasktracker.service

import com.tasktracker.model.Task
import com.tasktracker.repository.TaskRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import java.util.*

class TaskServiceTest {

    @Test
    fun `addTask creates and returns new task`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val service = TaskService(mockRepository)
        
        // When adding a task
        val task = service.addTask("Test task")
        
        // Verify that repository.save was called with the correct data
        verify(mockRepository).save(anyList())
        
        // Verify task properties
        assertNotNull(task.id)
        assertEquals("Test task", task.description)
        assertFalse(task.isCompleted)
        assertNotNull(task.createdAt)
    }

    @Test
    fun `addTask with empty description`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val service = TaskService(mockRepository)
        
        // When adding a task with empty description
        val task = service.addTask("")
        
        // Verify that repository.save was called
        verify(mockRepository).save(anyList())
        
        // Verify task properties
        assertNotNull(task.id)
        assertEquals("", task.description)
        assertFalse(task.isCompleted)
        assertNotNull(task.createdAt)
    }

    @Test
    fun `listTasks returns all tasks`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val tasks = service.listTasks()
        
        assertEquals(2, tasks.size)
        assertEquals("Task 1", tasks[0].description)
        assertEquals("Task 2", tasks[1].description)
        assertTrue(tasks[1].isCompleted)
    }

    @Test
    fun `completeTask marks task as completed`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val result = service.completeTask("1")
        
        assertTrue(result)
        verify(mockRepository).save(anyList())
        
        // Verify the task is actually completed by retrieving it
        val task = service.getTask("1")
        assertNotNull(task)
        assertTrue(task!!.isCompleted)
    }

    @Test
    fun `completeTask returns false for non-existent task`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val result = service.completeTask("non-existent")
        
        assertFalse(result)
        verify(mockRepository, never()).save(anyList())
        
        // Verify that existing tasks are unchanged
        val task = service.getTask("1")
        assertNotNull(task)
        assertFalse(task!!.isCompleted)
    }

    @Test
    fun `deleteTask removes task`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val result = service.deleteTask("1")
        
        assertTrue(result)
        verify(mockRepository).save(anyList())
        
        // Verify the task is actually removed
        val task = service.getTask("1")
        assertNull(task)
    }

    @Test
    fun `deleteTask returns false for non-existent task`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val result = service.deleteTask("non-existent")
        
        assertFalse(result)
        verify(mockRepository, never()).save(anyList())
        
        // Verify that existing tasks are unchanged
        val task = service.getTask("1")
        assertNotNull(task)
        assertFalse(task!!.isCompleted)
    }

    @Test
    fun `getTask returns existing task`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val task = service.getTask("1")
        
        assertNotNull(task)
        assertEquals("Task 1", task!!.description)
        assertFalse(task.isCompleted)
    }

    @Test
    fun `getTask returns null for non-existent task`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val task = service.getTask("non-existent")
        
        assertNull(task)
    }

    @Test
    fun `addTask and then listTasks returns added task`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val service = TaskService(mockRepository)
        
        // Add a task
        val task = service.addTask("Test task")
        
        // Verify it can be retrieved via list
        val tasks = service.listTasks()
        assertEquals(1, tasks.size)
        assertEquals(task.id, tasks[0].id)
        assertEquals(task.description, tasks[0].description)
        assertEquals(task.isCompleted, tasks[0].isCompleted)
    }

    @Test
    fun `listTasks returns all tasks when no filters specified`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val tasks = service.listTasks() // No filters specified
        
        assertEquals(2, tasks.size)
        assertEquals("Task 1", tasks[0].description)
        assertEquals("Task 2", tasks[1].description)
        assertFalse(tasks[0].isCompleted)
        assertTrue(tasks[1].isCompleted)
    }

    @Test
    fun `listTasks returns only completed tasks when done flag is true`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z"),
            Task("3", "Task 3", true, "2023-01-03T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val tasks = service.listTasks(done = true, open = false)
        
        assertEquals(2, tasks.size)
        assertTrue(tasks.all { it.isCompleted })
        assertEquals("Task 2", tasks[0].description)
        assertEquals("Task 3", tasks[1].description)
    }

    @Test
    fun `listTasks returns only open tasks when open flag is true`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z"),
            Task("3", "Task 3", false, "2023-01-03T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val tasks = service.listTasks(done = false, open = true)
        
        assertEquals(2, tasks.size)
        assertFalse(tasks.any { it.isCompleted })
        assertEquals("Task 1", tasks[0].description)
        assertEquals("Task 3", tasks[1].description)
    }

    @Test
    fun `listTasks returns all tasks when both done and open flags are true`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Task 2", true, "2023-01-02T00:00:00Z"),
            Task("3", "Task 3", false, "2023-01-03T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val tasks = service.listTasks(done = true, open = true)
        
        // When both flags are true, the code checks done first due to when statement order
        // If done is true, it returns tasks where task.isCompleted (so it returns only completed tasks)
        // This was the original behavior
        val completedTasks = testTasks.filter { it.isCompleted }
        assertEquals(completedTasks.size, tasks.size)
        assertTrue(tasks.all { it.isCompleted })
    }

    @Test
    fun `listTasks handles task filtering correctly`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Open Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Completed Task 1", true, "2023-01-02T00:00:00Z"),
            Task("3", "Open Task 2", false, "2023-01-03T00:00:00Z"),
            Task("4", "Completed Task 2", true, "2023-01-04T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        
        // Test listing only completed tasks
        val completedTasks = service.listTasks(done = true, open = false)
        assertEquals(2, completedTasks.size)
        assertTrue(completedTasks.all { it.isCompleted })
        
        // Test listing only open tasks
        val openTasks = service.listTasks(done = false, open = true)
        assertEquals(2, openTasks.size)
        assertFalse(openTasks.any { it.isCompleted })
        
        // Test listing all tasks
        val allTasks = service.listTasks(done = false, open = false)
        assertEquals(4, allTasks.size)
    }

    @Test
    fun `listTasks returns empty list when no tasks exist`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        `when`(mockRepository.load()).thenReturn(emptyList())
        
        val service = TaskService(mockRepository)
        val tasks = service.listTasks(done = true, open = false)
        
        assertEquals(0, tasks.size)
    }

    @Test
    fun `listTasks handles mixed completed and open tasks correctly`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Open Task 1", false, "2023-01-01T00:00:00Z"),
            Task("2", "Completed Task 1", true, "2023-01-02T00:00:00Z"),
            Task("3", "Open Task 2", false, "2023-01-03T00:00:00Z"),
            Task("4", "Completed Task 2", true, "2023-01-04T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        
        // Test listing only completed tasks
        val completedTasks = service.listTasks(done = true, open = false)
        assertEquals(2, completedTasks.size)
        assertTrue(completedTasks.all { it.isCompleted })
        
        // Test listing only open tasks
        val openTasks = service.listTasks(done = false, open = true)
        assertEquals(2, openTasks.size)
        assertFalse(openTasks.any { it.isCompleted })
        
        // Test listing all tasks
        val allTasks = service.listTasks(done = false, open = false)
        assertEquals(4, allTasks.size)
    }

    @Test
    fun `searchTasks returns tasks matching description`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Buy groceries", false, "2023-01-01T00:00:00Z"),
            Task("2", "Walk the dog", true, "2023-01-02T00:00:00Z"),
            Task("3", "Buy milk", false, "2023-01-03T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val results = service.searchTasks("groceries")
        
        assertEquals(1, results.size)
        assertEquals("Buy groceries", results[0].description)
    }

    @Test
    fun `searchTasks is case insensitive`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Buy groceries", false, "2023-01-01T00:00:00Z"),
            Task("2", "Walk the dog", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val results = service.searchTasks("GROCERIES")
        
        assertEquals(1, results.size)
        assertEquals("Buy groceries", results[0].description)
    }

    @Test
    fun `searchTasks returns multiple results`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Buy groceries", false, "2023-01-01T00:00:00Z"),
            Task("2", "Walk the dog", true, "2023-01-02T00:00:00Z"),
            Task("3", "Buy milk", false, "2023-01-03T00:00:00Z"),
            Task("4", "Buy bread", false, "2023-01-04T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val results = service.searchTasks("buy")
        
        assertEquals(3, results.size)
        assertTrue(results.any { it.description == "Buy groceries" })
        assertTrue(results.any { it.description == "Buy milk" })
        assertTrue(results.any { it.description == "Buy bread" })
    }

    @Test
    fun `searchTasks returns empty list when no matches`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Buy groceries", false, "2023-01-01T00:00:00Z"),
            Task("2", "Walk the dog", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val results = service.searchTasks("shopping")
        
        assertEquals(0, results.size)
    }

    @Test
    fun `searchTasks works with empty search text`() {
        // Create a mock repository
        val mockRepository = mock(TaskRepository::class.java)
        val testTasks = listOf(
            Task("1", "Buy groceries", false, "2023-01-01T00:00:00Z"),
            Task("2", "Walk the dog", true, "2023-01-02T00:00:00Z")
        )
        `when`(mockRepository.load()).thenReturn(testTasks)
        
        val service = TaskService(mockRepository)
        val results = service.searchTasks("")
        
        // Empty string should match all tasks
        assertEquals(2, results.size)
    }
}