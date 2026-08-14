/**
 * Entry point for the whole program.
 *
 * Uses a while True loop to interact with user.
 * Exits when user inputs command bye to interface.
 * 
 * @author Yong Kang Teo
 * @version 1.0
 */
public class TaskTracker {
	private final UserInterface ui;
	private final TaskList tasksList;

	public TaskTracker() {
		this.ui = new UserInterface();
		this.tasksList = new TaskList();
	}

	public void run() {
		ui.showWelcome();
		boolean isRunning = true;

		while (isRunning) {
			String input = ui.readCommand().trim();
			isRunning = processCommand(input);	
		}
	}

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
			ui.showMessage(tasksList.getFormattedList());
			return true;
		}

		// (possible) multi-word command routing with validation checks
		String[] parts = input.split(" ", 2);
		String command = parts[0].toLowerCase();
		String argument = parts.length > 1 ? parts[1].trim() : "";

		switch (command) {
			case "mark":
				ui.showMessage(tasksList.setTaskStatus(argument, true));
				break;
			case "unmark":
				ui.showMessage(tasksList.setTaskStatus(argument, false));
				break;
			default:
				ui.showMessage(tasksList.add(input));
				break;
		}
		return true;
	}
	public static void main(String[] args) {
		new TaskTracker().run();
	}
}
