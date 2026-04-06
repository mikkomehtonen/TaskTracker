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
    fun `service maintains internal state properly`() {
        // Create a mock repository that returns an empty list initially
        val mockRepository = mock(TaskRepository::class.java)
        `when`(mockRepository.load()).thenReturn(emptyList())
        
        val service = TaskService(mockRepository)
        
        // Add a task
        val task = service.addTask("Test task")
        
        // Load repository again should load the task
        `when`(mockRepository.load()).thenReturn(listOf(task))
        val service2 = TaskService(mockRepository)
        
        val tasks = service2.listTasks()
        assertEquals(1, tasks.size)
        assertEquals("Test task", tasks[0].description)
    }
}