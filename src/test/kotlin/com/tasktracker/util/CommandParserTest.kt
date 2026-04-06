package com.tasktracker.util

import com.tasktracker.util.CommandParser.Command
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class CommandParserTest {

    @Test
    fun `parse empty args returns Help command`() {
        val result = CommandParser.parse(emptyArray())
        assertTrue(result is Command.Help)
    }

    @Test
    fun `parse add command with description`() {
        val args = arrayOf("add", "Buy groceries")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Add)
        assertEquals("Buy groceries", (result as Command.Add).description)
    }

    @Test
    fun `parse add command with multiple words`() {
        val args = arrayOf("add", "Buy", "groceries", "and", "milk")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Add)
        assertEquals("Buy groceries and milk", (result as Command.Add).description)
    }

    @Test
    fun `parse add command with empty description`() {
        val args = arrayOf("add", "")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Add)
        assertEquals("", (result as Command.Add).description)
    }

    @Test
    fun `parse add command without description returns Error`() {
        val args = arrayOf("add")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Error)
        assertEquals("Usage: add \"<task description>\"", (result as Command.Error).message)
    }

    @Test
    fun `parse list command`() {
        val args = arrayOf("list")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.List)
    }

    @Test
    fun `parse list command with extra arguments`() {
        // List command doesn't validate extra arguments, they're ignored
        val args = arrayOf("list", "extra")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.List)
    }

    @Test
    fun `parse done command with valid ID`() {
        val args = arrayOf("done", "123-456-789")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Done)
        assertEquals("123-456-789", (result as Command.Done).id)
    }

    @Test
    fun `parse done command with extra arguments`() {
        // Done command doesn't validate extra arguments, they're ignored
        val args = arrayOf("done", "123-456-789", "extra", "args")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Done)
        assertEquals("123-456-789", (result as Command.Done).id)
    }

    @Test
    fun `parse done command without ID returns Error`() {
        val args = arrayOf("done")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Error)
        assertEquals("Usage: done <task_id>", (result as Command.Error).message)
    }

    @Test
    fun `parse remove command with valid ID`() {
        val args = arrayOf("remove", "123-456-789")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Remove)
        assertEquals("123-456-789", (result as Command.Remove).id)
    }

    @Test
    fun `parse remove command with extra arguments`() {
        // Remove command doesn't validate extra arguments, they're ignored
        val args = arrayOf("remove", "123-456-789", "extra", "args")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Remove)
        assertEquals("123-456-789", (result as Command.Remove).id)
    }

    @Test
    fun `parse remove command without ID returns Error`() {
        val args = arrayOf("remove")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Error)
        assertEquals("Usage: remove <task_id>", (result as Command.Error).message)
    }

    @Test
    fun `parse help command`() {
        val args = arrayOf("help")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Help)
    }

    @Test
    fun `parse unknown command returns Error`() {
        val args = arrayOf("unknown")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Error)
        assertTrue((result as Command.Error).message.contains("Unknown command"))
    }

    @Test
    fun `parse command with special characters`() {
        val args = arrayOf("add", "Buy \"groceries\" & milk")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Add)
        assertEquals("Buy \"groceries\" & milk", (result as Command.Add).description)
    }

    @Test
    fun `parse command with unicode characters`() {
        val args = arrayOf("add", "Buy cafés & résumés")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Add)
        assertEquals("Buy cafés & résumés", (result as Command.Add).description)
    }

    @Test
    fun `parse search command with text`() {
        val args = arrayOf("search", "groceries")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Search)
        assertEquals("groceries", (result as Command.Search).text)
    }

    @Test
    fun `parse search command with multiple words`() {
        val args = arrayOf("search", "buy", "groceries", "and", "milk")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Search)
        assertEquals("buy groceries and milk", (result as Command.Search).text)
    }

    @Test
    fun `parse search command without text returns Error`() {
        val args = arrayOf("search")
        val result = CommandParser.parse(args)
        assertTrue(result is Command.Error)
        assertEquals("Usage: search <text>", (result as Command.Error).message)
    }
}