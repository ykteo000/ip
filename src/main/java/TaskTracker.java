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
	public static void main(String[] args) {
		
		UserInterface ui = new UserInterface();
		ui.showWelcome();
		while (true) {

			String input = ui.readCommand();

			if (input.equalsIgnoreCase("bye")){
				break;
			}

			ui.showMessage(input);
		}

		ui.showGoodbye();
	}
}
