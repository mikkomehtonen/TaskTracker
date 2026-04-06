# TaskTracker

A simple CLI-based task management application built with Kotlin.

## Project Overview

This is a command-line task tracker that allows users to manage their tasks with basic functionality:
- Add new tasks
- List all tasks
- Mark tasks as completed
- Remove tasks
- Persistent storage using JSON file

## Project Structure

```
.
├── src/                  # Source code
│   └── main/
│       └── kotlin/
│           └── com/tasktracker/
│               ├── Main.kt           # Entry point
│               ├── model/            # Data models
│               │   └── Task.kt
│               ├── service/          # Business logic
│               │   └── TaskService.kt
│               ├── repository/       # Data persistence
│               │   └── TaskRepository.kt
│               └── util/             # Utility classes
│                   └── CommandParser.kt
├── build.gradle.kts      # Gradle build configuration
├── package.json          # Node.js dependencies
├── tasks.json            # Persistent storage for tasks
└── README.md             # This file
```

## Features

- **Add tasks**: `add "Task description"`
- **List tasks**: `list`
- **Complete tasks**: `done <task_id>`
- **Remove tasks**: `remove <task_id>`
- **Help information**: `help`
- **Persistent storage**: Tasks are saved to `tasks.json`

## Usage

The application is built as a Kotlin CLI application and can be run using Gradle:

```bash
# Build the application
./gradlew build

# Run the application
./gradlew run

# Or run directly with Kotlin
kotlin MainKt
```

## Core Components

### Main Application (`Main.kt`)
The entry point of the application that parses commands and interacts with the service layer.

### Task Model (`Task.kt`)
Represents a task with:
- `id`: Unique identifier for the task
- `description`: Description of the task
- `isCompleted`: Boolean flag indicating task completion status
- `createdAt`: Timestamp when the task was created

### Task Service (`TaskService.kt`)
Contains business logic for task management:
- Adding new tasks
- Listing all tasks
- Completing tasks
- Removing tasks

### Task Repository (`TaskRepository.kt`)
Handles data persistence using JSON serialization to `tasks.json` file.

### Command Parser (`CommandParser.kt`)
Parses command-line arguments and dispatches to appropriate actions.

## Example Usage

```
# Add a new task
./gradlew run --args='add "Buy groceries"'

# List all tasks
./gradlew run --args='list'

# Complete a task
./gradlew run --args='done f630dc4f-d79f-43af-a1ee-1c97a2f5f819'

# Remove a task
./gradlew run --args='remove f630dc4f-d79f-43af-a1ee-1c97a2f5f819'

# Show help
./gradlew run --args='help'
```

## Dependencies

- Kotlin 2.3.20
- Kotlinx Serialization 1.9.0
- Clikt 5.0.3 (for command-line argument parsing)

## Development

To contribute or extend:
1. Make changes to the source files in `src/main/kotlin/com/tasktracker/`
2. Run tests using `./gradlew test`
3. Build the application using `./gradlew build`
4. Use `./gradlew run` to test your changes

## License

This project is available under the MIT License.