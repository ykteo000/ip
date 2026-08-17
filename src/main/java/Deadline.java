/**
 * Represents a Deadline task that needs to be completed by a specific date or time.
 */
public class Deadline extends Task {
	protected String by;

	/**
	 * Constructs a Deadline instance with the specified description and deadline time.
	 *
	 * @param description Text describing the deadline task.
	 * @param by Due date or time string.
	 */
	public Deadline(String description, String by) {
		super(description);
		this.by = by;
	}

	@Override
	public String toString() {
		return "[D]" + super.toString() + " (by: " + by + ")";
	}

	@Override
	public String toFileFormat() {
		return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
	}
}
