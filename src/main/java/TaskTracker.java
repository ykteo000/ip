/**
 * Serves as the entry point and main controller for the application.
 * Manages the user interaction loop until the user chooses to exit.
 */
public class TaskTracker {
	private static final String DEFAULT_FILE_PATH = "./data/tasks.txt";
	private final UserInterface ui;
	private final Storage storage;
	private TaskList taskList;

	/**
	 * Initializes a new TaskTracker instance with initialized UI and TaskList.
	 */
	public TaskTracker() {
		this.ui = new UserInterface();
		this.storage = new Storage(DEFAULT_FILE_PATH);
	}

	/**
	 * Runs the main command processing loop until the exit command is received.
	 */
	public void run() {
		ui.showWelcome();

		try {
			taskList = new TaskList(storage.load());
		} catch (TaskTrackerException e) {
			ui.showMessage(e.getMessage());
			taskList = new TaskList();
		}

		boolean isRunning = true;

		while (isRunning) {
			String input = ui.readCommand().trim();
			try {
				isRunning = processCommand(input);
			} catch (TaskTrackerException e) {
				ui.showMessage(e.getMessage());
			}
		}
	}

	/**
	 * Processes a single user input command and executes the corresponding action.
	 *
	 * @param input User input string to process. Initial command is case-insensitive.
	 * @return Returns true if application should continue running, false if it should exit.
	 * @throws TaskTrackerException If input parsing fails or command is unrecognized.
	 */
	private boolean processCommand(String input) throws TaskTrackerException {
		// Exact single argument command checking
		if (input.isEmpty()) {
			return true;
		}

		// (possible) multi-word command routing with validation checks
		String[] parts = input.split(" ", 2);
		String commandWord = parts[0].toLowerCase();
		String argument = parts.length > 1 ? parts[1].trim() : "";

		CommandType command = CommandType.from(commandWord);
		switch (command) {
			case BYE:
				ui.showGoodbye();
				return false;
			case LIST:
				ui.showMessage(taskList.getFormattedList());
				break;
			case MARK:
				int markIndex = Parser.parseIndex(argument);
				ui.showMessage(taskList.setTaskStatus(markIndex, true));
				break;
			case UNMARK:
				int unmarkIndex = Parser.parseIndex(argument);
				ui.showMessage(taskList.setTaskStatus(unmarkIndex, false));
				break;
			case TODO:
				ToDo toDo = Parser.parseToDo(argument);
				ui.showMessage(taskList.add(toDo));
				break;
			case DEADLINE:
				Deadline deadline = Parser.parseDeadline(argument);
				ui.showMessage(taskList.add(deadline));
				break;
			case EVENT:
				Event event = Parser.parseEvent(argument);
				ui.showMessage(taskList.add(event));
				break;
			case DELETE:
				int deleteIndex = Parser.parseIndex(argument);
				ui.showMessage(taskList.deleteTask(deleteIndex));
				break;
			case HELP:
				ui.showHelp();
				break;
		}
		storage.save(taskList.getTasks());
		return true;
	}

	/**
	 * Starts the Task Tracker application.
	 */
	public static void main(String[] args) {
		new TaskTracker().run();
	}
}
