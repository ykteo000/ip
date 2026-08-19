/**
 * Container for all user-facing UI messages, application branding elements,
 * and command format error templates.
 * 
 * Centralizes static message strings across the application to ensure uniform
 * user feedback and simplify UI text maintenance.
 */
public class Message {

	private Message() {
		// Prevent instantiation
	}

	// --- Branding Elements --- //
	public static final String INDENT_4 = "    ";
	public static final String DIVIDER = "_".repeat(99);
	// Define the TaskTracker banner logo using external tool from manytools
	// Gemini AI was used to generate the specific formatting for the banner
	public static final String BANNER = "  ______           __                 \n"
		+ " /_  ______ ______/ /__                \n"
		+ "  / / / __ `/ ___/ //_/                \n"
		+ " / / / /_/ (__  / ,<                   \n"
		+ "/_______,_/____/_/|_|   __            \n"
		+ " /_  ___________ ______/ /_____  _____\n"
		+ "  / / / ___/ __ `/ ___/ //_/ _ \\/ ___/\n"
		+ " / / /  / /_/ / /__/ ,< /  __/ /    \n"
		+ "/_/ /_/   \\__,_/\\___/_/|_|\\___/_/     \n";


	public static final String MSG_WELCOME = "Hello there! I'm TaskTracker.\n"
			+ "I am a chatbot for you to track your tasks.\n"
			+ "What can I do for you today?\n"
			+ "Type 'help' to see available commands.\n";

	public static final String MSG_HELP = "Here are the available commands:\n"
            		+ "  - list : Views all tasks\n"
            		+ "  - todo <desc> : Adds a todo task\n"
			+ "  - deadline <desc> /by <date> : Adds a deadline task\n"
			+ "  - event <desc> /from <start> /to <end> : Adds an event task\n"
			+ "  - mark <index> : Marks a task as done\n"
			+ "  - unmark <index> : Marks a task as undone\n"
			+ "  - delete <index> : Deletes a task from the list\n"
			+ "  - bye : Exits the program\n";

	public static final String MSG_GOODBYE = "Baiiiiiii!!! Cya soon!\n";

	// --- Task Success Responses --- //
	public static final String MSG_TASK_ADDED = "Got it. I've added this task:\n";
    	public static final String MSG_TASK_REMOVED = "Noted. I've removed this task:\n";
    	public static final String MSG_TASK_MARKED = "Well done on completing this task! Marked as done! :D\n";
    	public static final String MSG_TASK_UNMARKED = "Awwh... marked this as undone. Try finish soon ya? :P\n";
    	public static final String MSG_NO_TASKS = "No tasks added yet, please add a task first!";

	// --- User Input Formats --- //
	public static final String INDEX_FORMAT = 
			"Format: mark <index> | unmark <index> | delete <index>\nIndex range: 1-100";
	public static final String TODO_FORMAT = "Format: todo <description>";
	public static final String DEADLINE_FORMAT = 
			"Format: deadline <description> /by <due date/ unknown date>";
	public static final String EVENT_FORMAT = 
			"Format: event <description> /from <start> /to <end>";

	// --- Error Messages --- //
	public static final String UNKNOWN_COMMAND = 
			"OOPS!! I'm sowwyyy :ccc\n"
			+ "I don't know what that command means ;(\n\n"
			+ "Type 'help' to see available commands.";
	public static final String ERR_TASK_LIST_EMPTY = "No task added yet, please add a task first!";
	public static final String ERR_TASK_LIST_FULL = "OOPS!! Task list full. Please remove any task first!";
	public static final String ERR_MISSING_INDEX = 
			"OOPS!! specify a task index number.\n" + INDEX_FORMAT;
	public static final String ERR_INVALID_INDEX = 
			"OOPS!! task number must be a valid integer.\n" + INDEX_FORMAT;
	public static final String ERR_EMPTY_TODO = 
			"OOPS!! todo description cannot be empty.\n" + TODO_FORMAT;
	public static final String ERR_EMPTY_DEADLINE = 
			"OOPS!! deadline description cannot be empty.\n" + DEADLINE_FORMAT;
	public static final String ERR_MISSING_BY = 
			"OOPS!! specify deadline description and date using '/by'.\n"
			+ DEADLINE_FORMAT;
	public static final String ERR_EMPTY_EVENT = 
			"OOPS!! event description cannot be empty.\n" + EVENT_FORMAT;
	public static final String ERR_MISSING_FROM = 
			"OOPS!! Please specify a valid start time using '/from'.\n" + EVENT_FORMAT;
	public static final String ERR_MISSING_TO = 
			"OOPS!! Please specify a valid end time using '/to'.\n" + EVENT_FORMAT;
}
