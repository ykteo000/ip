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
			isRunning = processCommand(input);	
		}
	}

	/**
     	* Processes a single user input command and executes the corresponding action.
     	*
     	* @param input User input string to process.
     	* @return Returns true if application should continue running, false if it should exit.
     	*/
	private boolean processCommand(String input) {
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
				ui.showMessage(taskList.setTaskStatus(argument, true));
				break;
			case "unmark":
				ui.showMessage(taskList.setTaskStatus(argument, false));
				break;
			case "todo":
			    Task toDoTask = new ToDo(argument);
			    ui.showMessage(taskList.add(toDoTask));
			    break;
			case "deadline":
				// Split "return book /by Sunday" into description and date
		    		String[] deadlineParts = argument.split(" /by ", 2);
		    		Task deadline = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
		    		ui.showMessage(taskList.add(deadline));
		    		break;
			case "event":
				// Split "project meeting /from Mon 2pm /to 4pm" 
			    	String[] fromParts = argument.split(" /from ", 2);
			    	String[] toParts = fromParts[1].split(" /to ", 2);
			    	Task event = new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
			    	ui.showMessage(taskList.add(event));
			    	break;
			default:
				Task taskToAdd = new Task(input);
				ui.showMessage(taskList.add(taskToAdd));
				break;
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
