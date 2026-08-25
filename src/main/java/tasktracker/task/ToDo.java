package tasktracker.task;

/**
 * Represents a ToDo task without any date or time constraints.
 */
public class ToDo extends Task {
	/**
	 * Constructs a ToDo instance with the specified task description.
	 *
	 * @param description Text describing the todo task.
	 */
	public ToDo(String description) {
		super(description);
	}

    /**
     * Returns the formatted string representation of the todo task.
     *
     * @return String representation containing the status and description.
     */
	@Override
	public String toString() {
		return "[T]" + super.toString();
	}

    /**
     * Formats the todo task for file storage.
     *
     * @return Pipe-delimited string representing the todo task.
     */
	@Override
	public String toFileFormat() {
		return "T | " + (isDone ? "1" : "0") + " | " + description;
	}
}

