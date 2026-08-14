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
	private final TaskList taskList;

	public TaskTracker() {
		this.ui = new UserInterface();
		this.taskList = new TaskList();
	}

	public void run() {
		
		UserInterface ui = new UserInterface();
		ui.showWelcome();
		while (true) {

			String input = ui.readCommand();

			if (input.equalsIgnoreCase("bye")) {
				break;
			} else if (input.equalsIgnoreCase("list")) {
				ui.showMessage(taskList.getFormattedList());
			} else {
				taskList.add(input);
				ui.showMessage("added: " + input);
			}
		}
		ui.showGoodbye();
	}

	public static void main(String[] args) {
		new TaskTracker().run();
	}
}
