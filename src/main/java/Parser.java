/**
 * Handles parsing and validation of raw user command strings.
 *OB
 * Note: Gemini AI was used substantially at this section to handle input edge cases.
 * Initial idea to create a dedicated parser class was by me, further refinement by AI.
 * Prompt "I currently have these methods in TaskTracker.java but I want to move them."
 * Prompt "How do I create a new Parser class that checks and validates input?"
 * Prompt "I basically only want new Task (or its subclasses) created post validation."
 * Prompt "Guide me on the process and explain to me the inner working mechanism."
 * Add code only after an intermediate level of understanding achieved.
 */
public class Parser {
	/**
	 * Parses and validates that the argument is a valid integer task index.
	 *
	 * @param argument The raw input string containing the task index.
	 * @return The parsed 1-based task index as an integer.
	 * @throws TaskTrackerException If the argument is empty or cannot be parsed into an integer.
	 */
	public static int parseIndex(String argument) throws TaskTrackerException {
		String trimmed = validateNonEmpty(argument, ErrorMessage.MISSING_INDEX);
		try {
			return Integer.parseInt(trimmed);
		} catch (NumberFormatException e) {
			throw new TaskTrackerException(ErrorMessage.INVALID_INDEX);
		}
	}

	/**
	 * Parses argument into a ToDo object.
	 *
	 * @param argument The raw input string containing the todo description.
	 * @return A new ToDo instance created from the parsed description.
	 * @throws TaskTrackerException If the description is empty or missing.
	 */
	public static ToDo parseToDo(String argument) throws TaskTrackerException {
		String description = validateNonEmpty(argument,ErrorMessage.EMPTY_TODO);
		
		return new ToDo(description);
	}

	/**
	 * Parses argument into a Deadline object.
	 *
	 * @param argument The raw input string containing the deadline description and date.
	 * @return A new Deadline instance created from the parsed description and by-date.
	 * @throws TaskTrackerException If the description or date is empty, or if '/by' is missing.
	 */
	public static Deadline parseDeadline(String argument) throws TaskTrackerException {
		validateNonEmpty(argument, ErrorMessage.EMPTY_DEADLINE);

		String[] parts = splitArgument(argument," /by ", ErrorMessage.MISSING_BY);

		return new Deadline(parts[0], parts[1]);
	}

	/**
	 * Parses argument into an Event object.
	 *
	 * @param argument The raw input string containing the event description, start time, and end time.
	 * @return A new Event instance created from the parsed description, start time, and end time.
	 * @throws TaskTrackerException If any field is empty, or if '/from' or '/to' specifiers are missing.
	 */
	public static Event parseEvent(String argument) throws TaskTrackerException {
		validateNonEmpty(argument, ErrorMessage.EMPTY_EVENT);

		String[] fromParts = splitArgument(argument," /from ", ErrorMessage.MISSING_FROM);

		String[] toParts = splitArgument(fromParts[1]," /to ", ErrorMessage.MISSING_TO);
		
		return new Event(fromParts[0], toParts[0], toParts[1]);
	}

	/**
	 * Ensures an argument string is non-empty after trimming.
	 *
	 * @param argument The raw argument string to check.
	 * @param errorMessage The exception message to throw if validation fails.
	 * @return The trimmed, non-empty argument string.
	 * @throws TaskTrackerException If the argument is null or empty after trimming.
	 */
	private static String validateNonEmpty(String argument, String errorMessage)
			throws TaskTrackerException {
		if (argument == null || argument.trim().isEmpty()) {
			throw new TaskTrackerException(errorMessage);
		}
		
		return argument.trim();
	}

	/**
	 * Splits an argument string using the given delimiter and validates both halves are non-empty.
	 *
	 * @param input The input string to split.
	 * @param delimiter The delimiter string to split on (e.g., " /by ").
	 * @param errorMessage The exception message to throw if splitting or validation fails.
	 * @return A two-element array containing trimmed substring parts [part1, part2].
	 * @throws TaskTrackerException If the delimiter is missing or either resulting part is empty.
	 */
	private static String[] splitArgument(String input, String delimiter, String errorMessage) 
		throws TaskTrackerException {
		String[] parts = input.split(delimiter, 2);
		
		if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
			throw new TaskTrackerException(errorMessage);
		}

		return new String[] {parts[0].trim(),parts[1].trim()};
	}
}
