import java.util.Scanner;
/**
 * Handles all user interface operations including reading input and displaying messages.
 */
public class UserInterface {	
	// Creates a scanner object to read user's inputs
	private final Scanner scanner;

	/**
	 * Initializes a new UserInterface instance with a System.in scanner.
	 */
	public UserInterface() {
		this.scanner = new Scanner(System.in);
	}

	/**
	 * Displays the welcome message and application banner.
	 */ 
	public void showWelcome() {
		System.out.println(Message.DIVIDER);
		System.out.println(Message.BANNER);
		showMessage(Message.MSG_WELCOME);
	}

	/**
	 * Reads a line of command input from the terminal.
	 *
	 * @return Returns the raw command string entered by the user.
	 */
	public String readCommand() {
		return scanner.nextLine();
	}

	/**
	 * Wraps and prints a message inside divider lines with standard indentation.
	 *
	 * @param message Message content to display.
	 */
	public void showMessage(String message) {
		System.out.println(Message.DIVIDER);
		String indentedMessage = Message.INDENT_4 + message.replace("\n", "\n" + Message.INDENT_4);
		System.out.println(indentedMessage);
		System.out.println(Message.DIVIDER);
	}

	/**
	 * Displays the farewell message upon exiting the application.
	 */
	public void showGoodbye() {
		System.out.println(Message.DIVIDER);
		System.out.println(Message.BANNER);
		showMessage(Message.MSG_GOODBYE);
	}

	/**
 	* Displays the help guide showing all available commands and their formats.
 	*/
	public void showHelp() {
    		showMessage(Message.MSG_HELP);
	}
}


