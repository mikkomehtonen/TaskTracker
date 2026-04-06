package com.tasktracker.util

object CommandParser {
    fun parse(args: Array<String>): Command {
        if (args.isEmpty()) {
            return Command.Help
        }

        return when (val command = args[0].lowercase()) {
            "add" -> {
                if (args.size < 2) {
                    Command.Error("Usage: add \"<task description>\"")
                } else {
                    Command.Add(args.drop(1).joinToString(" "))
                }
            }
            "list" -> {
                val flags = args.slice(1 until args.size).toSet()
                val done = flags.contains("--done")
                val open = flags.contains("--open")
                
                // If no flags are specified, show all tasks
                if (done || open) {
                    Command.List(done = done, open = open)
                } else {
                    Command.List(done = false, open = false)
                }
            }
            "done" -> {
                if (args.size < 2) {
                    Command.Error("Usage: done <task_id>")
                } else {
                    Command.Done(args[1])
                }
            }
            "remove" -> {
                if (args.size < 2) {
                    Command.Error("Usage: remove <task_id>")
                } else {
                    Command.Remove(args[1])
                }
            }
            "help" -> Command.Help
            else -> Command.Error("Unknown command: $command. Use 'help' for usage.")
        }
    }

    sealed class Command {
        object Help : Command()
        data class Add(val description: String) : Command()
        data class Done(val id: String) : Command()
        data class Remove(val id: String) : Command()
        data class Error(val message: String) : Command()
        data class List(val done: Boolean, val open: Boolean) : Command()
    }
}