package tasktracker;

import tasktracker.command.CommandType;
import tasktracker.exception.TaskTrackerException;
import tasktracker.parser.Parser;
import tasktracker.storage.Storage;
import tasktracker.task.Deadline;
import tasktracker.task.Event;
import tasktracker.task.FixedDurationTask;
import tasktracker.task.TaskList;
import tasktracker.task.ToDo;
import tasktracker.ui.Message;
import tasktracker.ui.UserInterface;

/**
 * Serves as the entry point and main controller for the application.
 * Manages the user interaction loop until the user chooses to exit.
 */
public class TaskTracker {
    private static final String DEFAULT_FILE_PATH = "./data/tasks.txt";
    private final UserInterface ui;
    private final Storage storage;
    private TaskList taskList;
    private CommandType lastCommandType;
    private boolean isErrorResponse = false;

    /**
     * Initializes a new TaskTracker instance with initialized UI and TaskList.
     */
    public TaskTracker() {
        this.ui = new UserInterface();
        this.storage = new Storage(DEFAULT_FILE_PATH);
        try {
            this.taskList = new TaskList(storage.load());
        } catch (TaskTrackerException e) {
            this.taskList = new TaskList();
        }
    }

    /**
     * Retrieves the welcome message for the GUI.
     */
    public String getWelcomeMessage() {
        return ui.getWelcomeMessage();
    }

    /**
     * Returns the command type of the most recently processed command.
     */
    public CommandType getLastCommandType() {
        return lastCommandType;
    }

    /**
     * Returns true if the most recent command execution resulted in an exception.
     */
    public boolean isErrorResponse() {
        return isErrorResponse;
    }

    /**
     * Processes input from the GUI and returns the bot's response message.
     *
     * @param input User input string from the GUI.
     * @return Response string to display in the chat dialog.
     */
    public String getResponse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        try {
            isErrorResponse = false;
            return processCommand(input.trim());
        } catch (TaskTrackerException e) {
            isErrorResponse = true;
            return e.getMessage();
        }
    }

    /**
     * Processes a single user input command, performs task mutations, and returns output.
     *
     * @param input Cleaned user input string.
     * @return The response message to show to the user.
     * @throws TaskTrackerException If input parsing fails or command is unrecognized.
     */
    private String processCommand(String input) throws TaskTrackerException {
        String[] parts = input.split(" ", 2);
        String commandWord = parts[0].toLowerCase();
        String argument = parts.length > 1 ? parts[1].trim() : "";

        CommandType command = CommandType.from(commandWord);
        this.lastCommandType = command;

        String response = executeCommand(command, argument);
        saveIfMutated(command);

        return response;
    }

    private String executeCommand(CommandType command, String argument) throws TaskTrackerException {
        switch (command) {
            case BYE:
                return ui.getGoodbyeMessage();
            case LIST:
                return taskList.getFormattedList();
            case MARK:
                return handleTaskStatusChange(argument, true);
            case UNMARK:
                return handleTaskStatusChange(argument, false);
            case TODO:
                return handleAddToDo(argument);
            case DEADLINE:
                return handleAddDeadline(argument);
            case EVENT:
                return handleAddEvent(argument);
            case FIXED:
                return handleAddFixedDurationTask(argument);
            case DELETE:
                return handleDeleteTask(argument);
            case FIND:
                return taskList.findTasks(Parser.parseFind(argument));
            case HELP:
                return Message.MSG_HELP;
            default:
                throw new TaskTrackerException(Message.ERR_UNKNOWN_COMMAND);
        }
    }

    private String handleTaskStatusChange(String argument, boolean isDone) throws TaskTrackerException {
        int index = Parser.parseIndex(argument);
        return taskList.setTaskStatus(index, isDone);
    }

    private String handleAddToDo(String argument) throws TaskTrackerException {
        ToDo toDo = Parser.parseToDo(argument);
        return taskList.add(toDo);
    }

    private String handleAddDeadline(String argument) throws TaskTrackerException {
        Deadline deadline = Parser.parseDeadline(argument);
        return taskList.add(deadline);
    }

    private String handleAddEvent(String argument) throws TaskTrackerException {
        Event event = Parser.parseEvent(argument);
        return taskList.add(event);
    }

    private String handleAddFixedDurationTask(String argument) throws TaskTrackerException {
        FixedDurationTask fixedTask = Parser.parseFixedDurationTask(argument);
        return taskList.add(fixedTask);
    }

    private String handleDeleteTask(String argument) throws TaskTrackerException {
        int index = Parser.parseIndex(argument);
        return taskList.deleteTask(index);
    }

    /**
     * Persists tasks to disk if the command alters the task list state.
     */
    private void saveIfMutated(CommandType command) throws TaskTrackerException {
        switch (command) {
            case MARK:
            case UNMARK:
            case TODO:
            case DEADLINE:
            case EVENT:
            case FIXED:
            case DELETE:
                storage.save(taskList.getTasks());
                break;
            default:
                break;
        }
    }

    /**
     * Runs the legacy CLI main command processing loop until an exit command is received.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand().trim();
            if (input.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }
            String response = getResponse(input);
            ui.showMessage(response);
        }
    }

    /**
     * Starts the Task Tracker application.
     *
     * @param args Command-line arguments passed during startup.
     */
    public static void main(String[] args) {
        new TaskTracker().run();
    }
}
