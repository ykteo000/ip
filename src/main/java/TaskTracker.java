/**
 * Serves as the entry point and main controller for the application.
 * Manages the user interaction loop until the user chooses to exit.
 */
public class TaskTracker {
	private final UserInterface ui;
	private final TaskList taskList;

	/**
     	* Initializes a new TaskTracker instance with initialized UI and TaskList.
     	*/
	public TaskTracker() {
		this.ui = new UserInterface();
		this.taskList = new TaskList();
	}

	/**
     	* Runs the main command processing loop until the exit command is received.
     	*/
	public void run() {
		ui.showWelcome();
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

		if (input.equalsIgnoreCase("bye")) {
			ui.showGoodbye();
			return false;
		}

		if (input.equalsIgnoreCase("list")) {
			ui.showMessage(taskList.getFormattedList());
			return true;
		}

		// (possible) multi-word command routing with validation checks
		String[] parts = input.split(" ", 2);
		String command = parts[0].toLowerCase();
		String argument = parts.length > 1 ? parts[1].trim() : "";

		switch (command) {
			case "mark":
				int markIndex = Parser.parseIndex(argument);
				ui.showMessage(taskList.setTaskStatus(markIndex, true));
				break;
			case "unmark":
				int unmarkIndex = Parser.parseIndex(argument);
				ui.showMessage(taskList.setTaskStatus(unmarkIndex, false));
				break;
			case "todo":
			    	ToDo toDo = Parser.parseToDo(argument);
			    	ui.showMessage(taskList.add(toDo));
			    	break;
			case "deadline":
			    	Deadline deadline = Parser.parseDeadline(argument);
			    	ui.showMessage(taskList.add(deadline));
			    	break;
			case "event":
			    	Event event = Parser.parseEvent(argument);
			    	ui.showMessage(taskList.add(event));
			    	break;
			case "--help":
			case "help":
				ui.showHelp();
				break;
			default:
				throw new TaskTrackerException("OOPS!! I'm sorry :ccc\n"
						+ "I don't know what that command means ;(\n\n"
						+ "Type 'help' to see available commands.\n");
		}
		return true;
	}

	/**
     	* Starts the Task Tracker application.
     	*/
	public static void main(String[] args) {
		new TaskTracker().run();
	}
}
