import java.util.Scanner;
/**
 * Handles i/o by end user.
 *
 * showWelcome, showMessage and showGoodbye methods used.
 * Easier readibility for future potential changes.
 * 
 * @author Yong Kang Teo
 * @version 1.0
 */

public class UserInterface{	
	
	// Define indent of 4 spaces
	public static final String INDENT_4 = "    ";
	
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

	public UserInterface() {
		this.scanner = new Scanner(System.in);
	}

	// Welcome message abstracted from TaskTracker.java and included here as UI Object. 
	public void showWelcome() {
		System.out.println(DIVIDER);
		System.out.println(BANNER);
		System.out.println("Hello there! I'm TaskTracker.");
		System.out.println("I am a chatbot used to track your tasks.\n");
		System.out.println("What can I do for you today?\n");
		System.out.println(DIVIDER);
	}

	// Reads user input from terminal
	public String readCommand() {
		return scanner.nextLine();
	}

	// Wraps output messages inside divider lines
	public void showMessage(String message) {
		System.out.println(DIVIDER);
		String indentedMessage = INDENT_4 + message.replace("\n", "\n" + INDENT_4);
		System.out.println(indentedMessage);
		System.out.println(DIVIDER);
	}

	// Exits system upon bye command input by user
	public void showGoodbye() {
		System.out.println(DIVIDER);
                System.out.println(INDENT_4 + "Bye!!! Cya soon!\n");
                System.out.println(DIVIDER);
	}
}


