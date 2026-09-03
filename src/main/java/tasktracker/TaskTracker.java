package tasktracker;

import tasktracker.command.CommandType;
import tasktracker.exception.TaskTrackerException;
import tasktracker.parser.Parser;
import tasktracker.storage.Storage;
import tasktracker.task.Deadline;
import tasktracker.task.Event;
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

        boolean isMutated = false;
        String response;

        CommandType command = CommandType.from(commandWord);
        this.lastCommandType = command;
        switch (command) {
            case BYE:
                response = ui.getGoodbyeMessage();
                break;
            case LIST:
                response = taskList.getFormattedList();
                break;
            case MARK:
                int markIndex = Parser.parseIndex(argument);
                response = taskList.setTaskStatus(markIndex, true);
                isMutated = true;
                break;
            case UNMARK:
                int unmarkIndex = Parser.parseIndex(argument);
                response = taskList.setTaskStatus(unmarkIndex, false);
                isMutated = true;
                break;
            case TODO:
                ToDo toDo = Parser.parseToDo(argument);
                response = taskList.add(toDo);
                isMutated = true;
                break;
            case DEADLINE:
                Deadline deadline = Parser.parseDeadline(argument);
                response = taskList.add(deadline);
                isMutated = true;
                break;
            case EVENT:
                Event event = Parser.parseEvent(argument);
                response = taskList.add(event);
                isMutated = true;
                break;
            case DELETE:
                int deleteIndex = Parser.parseIndex(argument);
                response = taskList.deleteTask(deleteIndex);
                isMutated = true;
                break;
            case FIND:
                String keyword = Parser.parseFind(argument);
                response = taskList.findTasks(keyword);
                break;
            case HELP:
                response = Message.MSG_HELP; // Or whatever string ui.showHelp() outputs
                break;
            default:
                throw new TaskTrackerException(Message.ERR_UNKNOWN_COMMAND);
        }

        if (isMutated) {
            storage.save(taskList.getTasks());
        }

        return response;
    }

    /**
     * Legacy Terminal CLI Runner
     * Runs the main command processing loop until the exit command is received.
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
