package tasktracker.parser;

import tasktracker.exception.TaskTrackerException;
import tasktracker.task.Deadline;
import tasktracker.task.Event;
import tasktracker.task.TaskDateTime;
import tasktracker.task.ToDo;
import tasktracker.ui.Message;

/**
 * Handles parsing and validation of raw user command strings.
 * <p>
 * Note: Gemini AI was used substantially at this section to handle input edge cases.
 * Initial idea to create a dedicated parser class was by me, further refinement by AI.
 * Prompt "I currently have these methods in TaskTracker.java, but I want to move them."
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
        String trimmed = validateNonEmpty(argument, Message.ERR_MISSING_INDEX);
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            throw new TaskTrackerException(Message.ERR_INVALID_INDEX);
        }
    }

    /**
     * Parses and validates the search keyword argument for the find command.
     *
     * @param argument The raw input string containing the search keyword.
     * @return The trimmed search keyword.
     * @throws TaskTrackerException If the keyword is missing or empty.
     */
    public static String parseFind(String argument) throws TaskTrackerException {
        return validateNonEmpty(argument, Message.ERR_EMPTY_FIND);
    }

    /**
     * Parses argument into a ToDo object.
     *
     * @param argument The raw input string containing the todo description.
     * @return A new ToDo instance created from the parsed description.
     * @throws TaskTrackerException If the description is empty or missing.
     */
    public static ToDo parseToDo(String argument) throws TaskTrackerException {
        String description = validateNonEmpty(argument, Message.ERR_EMPTY_TODO);

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
        validateNonEmpty(argument, Message.ERR_EMPTY_DEADLINE);

        String[] parts = splitArgument(argument, " /by ", Message.ERR_MISSING_BY);
        String description = validateNonEmpty(parts[0], Message.ERR_EMPTY_DEADLINE);
        String by = validateNonEmpty(parts[1], Message.ERR_MISSING_BY);

        return new Deadline(description, new TaskDateTime(by));
    }

    /**
     * Parses argument into an Event object.
     * <p>
     * Note: Gemini AI used to make the parseEvent validation better to handle improper user input.
     *
     * @param argument The raw input string containing the event description, start time, and end time.
     * @return A new Event instance created from the parsed description, start time, and end time.
     * @throws TaskTrackerException If any field is empty, or if '/from' or '/to' specifiers are missing.
     */
    public static Event parseEvent(String argument) throws TaskTrackerException {
        String trimmed = validateNonEmpty(argument, Message.ERR_EMPTY_EVENT);

        // 1. Inverted ordering check
        if (trimmed.contains(" /to ") && trimmed.contains(" /from ")
                && trimmed.indexOf(" /to ") < trimmed.indexOf(" /from ")) {
            throw new TaskTrackerException(Message.ERR_OUT_OF_ORDER);
        }

        // 2. Extract description and the rest using splitArgument
        String[] fromParts = splitArgument(trimmed, " /from ", Message.ERR_MISSING_FROM);
        String rawDescription = fromParts[0];
        String afterFrom = fromParts[1];

        // 3. Extract /from and /to segments using splitArgument
        String[] toParts = splitArgument(afterFrom, " /to ", Message.ERR_MISSING_TO);
        String rawFromStr = toParts[0];
        String rawToStr = toParts[1];

        // 4. Validate non-empty AND reject '|' characters
        String description = validateNonEmpty(rawDescription, Message.ERR_EMPTY_EVENT);
        String fromStr = validateNonEmpty(rawFromStr, Message.ERR_MISSING_FROM);
        String toStr = validateNonEmpty(rawToStr, Message.ERR_MISSING_TO);

        // 5. Parse date-times
        TaskDateTime startDateTime = new TaskDateTime(fromStr);
        TaskDateTime endDateTime = new TaskDateTime(toStr);

        // 6. Enforce chronological sequence
        if (startDateTime.getDateTime().isAfter(endDateTime.getDateTime())) {
            throw new TaskTrackerException(Message.ERR_EVENT_CHRONOLOGY);
        }
        assert !startDateTime.getDateTime().isAfter(endDateTime.getDateTime())
                : "Event start date must be before or equal to end date.";

        return new Event(description, startDateTime, endDateTime);
    }

    /**
     * Ensures an argument string is non-empty after trimming.
     *
     * @param argument     The raw argument string to check.
     * @param errorMessage The exception message to throw if validation fails.
     * @return The trimmed, non-empty argument string.
     * @throws TaskTrackerException If the argument is null or empty after trimming.
     */
    private static String validateNonEmpty(String argument, String errorMessage)
            throws TaskTrackerException {
        assert errorMessage != null && !errorMessage.trim().isEmpty()
                : "Error message passed to validateNonEmpty must not be blank.";
        if (argument == null || argument.trim().isEmpty()) {
            throw new TaskTrackerException(errorMessage);
        } else if (argument.contains("|")) {
            throw new TaskTrackerException(Message.ERR_NO_DELIMITER);
        }
        return argument.trim();
    }

    /**
     * Splits an argument string using the given delimiter and validates both halves are non-empty.
     *
     * @param input        The input string to split.
     * @param delimiter    The delimiter string to split on (e.g., " /by ").
     * @param errorMessage The exception message to throw if splitting or validation fails.
     * @return A two-element array containing trimmed substring parts [part1, part2].
     * @throws TaskTrackerException If the delimiter is missing or either resulting part is empty.
     */
    private static String[] splitArgument(String input, String delimiter, String errorMessage)
            throws TaskTrackerException {
        assert delimiter != null && !delimiter.isEmpty()
                : "Delimiter passed to splitArgument must not be empty.";
        assert errorMessage != null && !errorMessage.trim().isEmpty()
                : "Error message passed to splitArgument must not be blank.";

        String[] parts = input.split(delimiter, 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new TaskTrackerException(errorMessage);
        }

        return new String[]{parts[0].trim(), parts[1].trim()};
    }

}
