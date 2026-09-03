package tasktracker.task;

/**
 * Represents a Deadline task that needs to be completed by a specific date or time.
 */
public class Deadline extends Task {
    protected TaskDateTime by;

    /**
     * Constructs a Deadline instance with the specified description and deadline time.
     *
     * @param description Text describing the deadline task.
     * @param by          Due date or time string.
     */
    public Deadline(String description, TaskDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the formatted string representation of the deadline task.
     *
     * @return String representation containing status, description, and due date.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.toDisplayString() + ")";
    }

    /**
     * Formats the deadline task for file storage.
     *
     * @return Pipe-delimited string representing the deadline task.
     */
    @Override
    public String toFileFormat() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by.toFileString();
    }
}
