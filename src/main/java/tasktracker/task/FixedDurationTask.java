package tasktracker.task;

/**
 * Represents a task that requires a fixed amount of time to complete.
 * Unlike deadlines or events, a fixed-duration task is not tied to a specific
 * calendar date or time window, only a required duration.
 */
public class FixedDurationTask extends Task {
    /** The duration required to complete the task. */
    private final String duration;

    /**
     * Constructs an uncompleted {@code FixedDurationTask} with the given description and duration.
     *
     * @param description The textual details of the task.
     * @param duration The duration needed to complete the task (e.g., "2 hours").
     */
    public FixedDurationTask(String description, String duration) {
        super(description);
        this.duration = duration;
    }

    /**
     * Constructs a {@code FixedDurationTask} with the given description, duration, and completion status.
     * Typically used when reconstructing tasks from persistent storage.
     *
     * @param description The textual details of the task.
     * @param duration The duration needed to complete the task.
     * @param isDone The completion status of the task.
     */
    public FixedDurationTask(String description, String duration, boolean isDone) {
        super(description);
        this.duration = duration;
        if (isDone) {
            this.markAsDone();
        }
    }

    /**
     * Returns the required duration of the task.
     *
     * @return A string representing the task's duration.
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Formats the task details into a delimited string suitable for file storage.
     *
     * @return The serialized string representation for file persistence.
     */
    @Override
    public String toFileFormat() {
        return "F | " + toFileFormatPrefix() + " | " + duration;
    }

    /**
     * Returns the formatted string representation of the fixed-duration task for display to the user.
     *
     * @return A string containing the task type indicator, status icon, description, and required duration.
     */
    @Override
    public String toString() {
        return "[F]" + super.toString() + " (needs: " + duration + ")";
    }
}
