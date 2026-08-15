import java.util.Scanner;
/**
 * Handles all user interface operations including reading input and displaying messages.
 */
public class UserInterface {	

	// Define indent of 4 spaces
	private static final String INDENT_4 = "    ";

	// Define a divider line using underscores
	private static final String DIVIDER = "_".repeat(99);

	// Define the TaskTracker banner logo using external tool from manytools
	// Gemini AI was used to generate the specific formatting for the banner
	private static final String BANNER = "  ______           __                 \n"
		+ " /_  ______ ______/ /__                \n"
		+ "  / / / __ `/ ___/ //_/                \n"
		+ " / / / /_/ (__  / ,<                   \n"
		+ "/_______,_/____/_/|_|   __            \n"
		+ " /_  ___________ ______/ /_____  _____\n"
		+ "  / / / ___/ __ `/ ___/ //_/ _ \\/ ___/\n"
		+ " / / /  / /_/ / /__/ ,< /  __/ /    \n"
		+ "/_/ /_/   \\__,_/\\___/_/|_|\\___/_/     \n";

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
		System.out.println(DIVIDER);
		System.out.println(BANNER);
		System.out.println("Hello there! I'm TaskTracker.");
		System.out.println("I am a chatbot used to track your tasks.\n");
		System.out.println("What can I do for you today?\n");
		System.out.println("Type 'help' to see available commands.\n");
		System.out.println(DIVIDER);
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
		System.out.println(DIVIDER);
		String indentedMessage = INDENT_4 + message.replace("\n", "\n" + INDENT_4);
		System.out.println(indentedMessage);
		System.out.println(DIVIDER);
	}

	/**
	 * Displays the farewell message upon exiting the application.
	 */
	public void showGoodbye() {
		System.out.println(DIVIDER);
		System.out.println(INDENT_4 + "Baiiiiiii!!! Cya soon!\n");
		System.out.println(DIVIDER);
	}


	/**
 	* Displays the help guide showing all available commands and their formats.
 	*/
	public void showHelp() {
    		String helpMessage = "Here are the available commands:\n"
            			+ "  - list : Views all tasks\n"
            			+ "  - todo <desc> : Adds a todo task\n"
				+ "  - deadline <desc> /by <date> : Adds a deadline task\n"
				+ "  - event <desc> /from <start> /to <end> : Adds an event task\n"
				+ "  - mark <index> : Marks a task as done\n"
				+ "  - unmark <index> : Marks a task as undone\n"
				+ "  - bye : Exits the program";
    		showMessage(helpMessage);
	}
}


