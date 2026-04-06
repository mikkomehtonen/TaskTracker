#!/bin/bash

# TaskTracker CLI Script
# Easy way to run the TaskTracker application

# Get the directory where this script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Change to the script's directory
cd "$SCRIPT_DIR"

# Function to show usage
usage() {
    echo "Usage: $0 [command] [arguments]"
    echo ""
    echo "Commands:"
    echo "  add 'task description'     - Add a new task"
    echo "  list                       - List all tasks"
    echo "  list --done                - List completed tasks only"
    echo "  list --open                - List open tasks only"
    echo "  done id                    - Mark task as complete"
    echo "  remove id                  - Remove a task"
    echo "  search 'text'              - Search tasks by description"
    echo "  help                       - Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 add 'Buy groceries'"
    echo "  $0 list"
    echo "  $0 list --done"
    echo "  $0 list --open"
    echo "  $0 done 1"
    echo "  $0 remove 1"
    echo "  $0 search 'groceries'"
    echo "  $0 help"
}

# Function to run the application with arguments
run_app() {
    ./gradlew run --args="$*"
}

# Main logic
if [ $# -eq 0 ]; then
    usage
    exit 1
fi

# Process commands
case "$1" in
    add)
        if [ $# -lt 2 ]; then
            echo "Error: Missing task description"
            echo "Usage: $0 add 'task description'"
            exit 1
        fi
        # Shift to remove the command and pass the rest as arguments
        shift
        run_app "add" "$*"
        ;;
    list)
        # Pass all arguments to get --done and --open flags working
        all_args="list"
        shift
        if [ $# -gt 0 ]; then
            for arg in "$@"; do
                all_args="$all_args $arg"
            done
        fi
        run_app "$all_args"
        ;;
    done)
        if [ $# -lt 2 ]; then
            echo "Error: Missing task ID"
            echo "Usage: $0 done id"
            exit 1
        fi
        shift
        run_app "done" "$*"
        ;;
    remove)
        if [ $# -lt 2 ]; then
            echo "Error: Missing task ID"
            echo "Usage: $0 remove id"
            exit 1
        fi
        shift
        run_app "remove" "$*"
        ;;
    search)
        if [ $# -lt 2 ]; then
            echo "Error: Missing search text"
            echo "Usage: $0 search 'text'"
            exit 1
        fi
        # Shift to remove the command and pass the rest as arguments
        shift
        run_app "search" "$*"
        ;;
    help)
        usage
        ;;
    *)
        echo "Unknown command: $1"
        echo "Use '$0 help' for usage information"
        exit 1
        ;;
esac