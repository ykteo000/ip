/**
 * Container for user-facing error messages and command format templates.
 * Centralizes all error strings used across parsers to ensure consistent messaging.
 */
public class ErrorMessage {
	private static final String INDEX_FORMAT = 
			"Format: mark <index> | unmark <index> | delete <index>\nIndex range: 1-100";

	private static final String TODO_FORMAT = "Format: todo <description>";

	private static final String DEADLINE_FORMAT = 
			"Format: deadline <description> /by <due date/ unknown date>";

	private static final String EVENT_FORMAT = 
			"Format: event <description> /from <start> /to <end>";
	
	public static final String UNKNOWN_COMMAND = 
			"OOPS!! I'm sowwyyy :ccc\n"
			+ "I don't know what that command means ;(\n\n"
			+ "Type 'help' to see available commands.";

	public static final String MISSING_INDEX = 
			"OOPS!! specify a task index number.\n" + INDEX_FORMAT;

	public static final String INVALID_INDEX = 
			"OOPS!! task number must be a valid integer.\n" + INDEX_FORMAT;

	public static final String EMPTY_TODO = 
			"OOPS!! todo description cannot be empty.\n" + TODO_FORMAT;

	public static final String EMPTY_DEADLINE = 
			"OOPS!! deadline description cannot be empty.\n" + DEADLINE_FORMAT;

	public static final String MISSING_BY = 
			"OOPS!! specify deadline description and date using '/by'.\n"
			+ DEADLINE_FORMAT;

	public static final String EMPTY_EVENT = 
			"OOPS!! event description cannot be empty.\n" + EVENT_FORMAT;

	public static final String MISSING_FROM = 
			"OOPS!! Please specify a valid start time using '/from'.\n" + EVENT_FORMAT;

	public static final String MISSING_TO = 
			"OOPS!! Please specify a valid end time using '/to'.\n" + EVENT_FORMAT;
}
