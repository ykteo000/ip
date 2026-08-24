package tasktracker.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import tasktracker.exception.TaskTrackerException;
import tasktracker.ui.Message;

/**
 * Represents a date and time wrapper for tasks, handling parsing,
 * UI display formatting, and file save formatting.
 */
public class TaskDateTime {
    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter DISPLAY_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("MMM d yyyy, h:mm a")
            .toFormatter(Locale.ENGLISH);

    private static final DateTimeFormatter FILE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private final LocalDateTime dateTime;

    public TaskDateTime(String rawDateTime) throws TaskTrackerException {
        String trimmed = rawDateTime.trim();
        LocalDateTime parsed;
        try {
            // Standard CLI input: 2026-12-31 2359
            parsed = LocalDateTime.parse(trimmed, INPUT_FORMATTER);
        } catch (DateTimeParseException e1) {
            try {
                // Fallback for saved display formats: Dec 31 2026, 11:59 pm / PM
                parsed = LocalDateTime.parse(trimmed, DISPLAY_FORMATTER);
            } catch (DateTimeParseException e2) {
                throw new TaskTrackerException(Message.ERR_INVALID_DATE_TIME);
            }
        }
        this.dateTime = parsed;
    }

    public TaskDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String toDisplayString() {
        return dateTime.format(DISPLAY_FORMATTER);
    }

    public String toFileString() {
        return dateTime.format(FILE_FORMATTER);
    }

    @Override
    public String toString() {
        return toDisplayString();
    }
}
