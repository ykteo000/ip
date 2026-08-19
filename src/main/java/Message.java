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

	// --- Raw Command Syntax (private helpers) --- //
	private static final String SYNTAX_INDEX = "mark <index> | unmark <index> | delete <index>";
	private static final String SYNTAX_TODO = "todo <description>";
	private static final String SYNTAX_DEADLINE = "deadline <description> /by <due date/ unknown date>";
	private static final String SYNTAX_EVENT = "event <description> /from <start> /to <end>";
	private static final String SYNTAX_VALID_RANGE = "Index range: ";
	private static final String SYNTAX_FORMAT = "Format: ";

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

	public static final String MSG_GOODBYE = "Baiiiiiii!!! Cya soon!\n";

	public static final String MSG_HELP = "Here are the available commands:\n"
            		+ "  - list : Views all tasks\n"
			+ "  - " + SYNTAX_TODO + " : Adds a todo task\n"
        		+ "  - " + SYNTAX_DEADLINE + " : Adds a deadline task\n"
        		+ "  - " + SYNTAX_EVENT + " : Adds an event task\n"
        		+ "  - " + SYNTAX_INDEX + " : Manipulates task by index\n"
        		+ "  - bye : Exits the program\n";

	// --- Task Success Responses --- //
	public static final String MSG_TASK_ADDED = "Got it. I've added this task:\n";
    	public static final String MSG_TASK_REMOVED = "Noted. I've removed this task:\n";
    	public static final String MSG_TASK_MARKED = "Well done on completing this task! Marked as done! :D\n";
    	public static final String MSG_TASK_UNMARKED = "Awwh... marked this as undone. Try finish soon ya? :P\n";
    	public static final String MSG_NO_TASKS = "No tasks added yet, please add a task first!\n";

	// --- User Input Formats --- //
	public static final String INDEX_FORMAT = SYNTAX_FORMAT + SYNTAX_INDEX + "\n"; 
	public static final String TODO_FORMAT = SYNTAX_FORMAT + SYNTAX_TODO + "\n";
	public static final String DEADLINE_FORMAT = SYNTAX_FORMAT + SYNTAX_DEADLINE + "\n";
	public static final String EVENT_FORMAT = SYNTAX_FORMAT + SYNTAX_EVENT + "\n";

	// --- Error Messages --- //
	public static final String UNKNOWN_COMMAND = 
			"OOPS!! I'm sowwyyy :ccc\n"
			+ "I don't know what that command means ;(\n"
			+ "Type 'help' to see available commands.\n";
	public static final String ERR_TASK_LIST_EMPTY = "No task added yet, please add a task first!\n";
	public static final String ERR_TASK_LIST_FULL = "OOPS!! Task list full. Please remove any task first!\n";
	public static final String ERR_MISSING_INDEX = 
			"OOPS!! specify a task index number.\n" + INDEX_FORMAT;
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
	public static final String ERR_INVALID_INDEX =
			"OOPS!! task number must be a valid integer.\n" + INDEX_FORMAT;
	public static String MSG_TASK_COUNT(int count) {
		return "Now you have " + count + " task" + (count == 1 ? "" : "s") + " in the list.\n";
	}
	public static String ERR_OUT_OF_BOUNDS(int count) {
    		return "OOPS!! Task index out of bounds.\n" + INDEX_FORMAT + SYNTAX_VALID_RANGE
				+ (count == 0 ? "No tasks available" : "1-" + count) + "\n";
	}
}
