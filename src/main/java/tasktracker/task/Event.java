package tasktracker.task;

/**
 * Represents an Event task that occurs within a specified start and end time range.
 */
public class Event extends Task {
	protected TaskDateTime from;
	protected TaskDateTime to;

	/**
	 * Constructs an Event instance with the specified description, start time, and end time.
	 *
	 * @param description Text describing the event task.
	 * @param from Start time or start date description.
	 * @param to End time or end date description.
	 */
	public Event(String description, TaskDateTime from, TaskDateTime to) {
		super(description);
		this.from = from;
		this.to = to;
	}

    /**
     * Returns the formatted string representation of the event task.
     *
     * @return String representation containing status, description, start time, and end time.
     */
	@Override
	public String toString() {
		return "[E]" + super.toString() + " (from: " + from.toDisplayString()
				+ " to: " + to.toDisplayString() + ")";
	}

    /**
     * Formats the event task for file storage.
     *
     * @return Pipe-delimited string representing the event task.
     */
	@Override
	public String toFileFormat() {
		return "E | " + (isDone ? "1" : "0") + " | " + description + " | "
				+ from.toFileString() + " | " + to.toFileString();
	}
}
