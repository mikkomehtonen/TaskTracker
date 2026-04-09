# TaskTracker Agent Instructions

This is a Kotlin-based CLI task management application with the following key characteristics:

## Project Structure
- Main application entry point: `src/main/kotlin/com/tasktracker/Main.kt`
- Core components:
  - Task model: `src/main/kotlin/com/tasktracker/model/Task.kt`
  - Task service: `src/main/kotlin/com/tasktracker/service/TaskService.kt`
  - Task repository: `src/main/kotlin/com/tasktracker/repository/TaskRepository.kt`
  - Command parser: `src/main/kotlin/com/tasktracker/util/CommandParser.kt`

## Key Commands
- Build: `./gradlew build`
- Run: `./gradlew run` or `./tasktracker.sh` (wrapper script)
- Test: `./gradlew test`

## Important Implementation Details
- Tasks are stored in `tasks.json` file
- Application uses Kotlin 2.3.20 and JVM 21
- Uses kotlinx-serialization for JSON handling
- Uses Clikt for command-line argument parsing
- Tests use JUnit 5 and Mockito for mocking
- Colorized output for completed tasks (green text)

## Testing Considerations
- Tests use mock repositories to isolate service logic
- Tests cover all major functionality including edge cases
- Test suite can be run with `./gradlew test`
- Test coverage is generated automatically via JaCoCo

## Usage Patterns
- Arguments to `./gradlew run` should be passed with `--args` flag
- Wrapper script `tasktracker.sh` provides convenient command aliases
- The application persists data between runs in `tasks.json`
- Commands are case-sensitive

## Framework Quirks
- Application uses Kotlin's `when` expression for command handling
- Task IDs are UUIDs generated at runtime
- Completed tasks are displayed in green text
- Search functionality is case-insensitive
- List command accepts `--done` and `--open` flags for filtering

## Entry Points
The main entry point is `Main.kt` which coordinates between command parser, service, and repository components. The main flow is:
1. Parse command-line arguments
2. Execute appropriate service operation
3. Update repository and return result