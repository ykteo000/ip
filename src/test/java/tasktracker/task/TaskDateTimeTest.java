package tasktracker.task;

import org.junit.jupiter.api.Test;
import tasktracker.exception.TaskTrackerException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskDateTimeTest {

    @Test
    public void constructor_validStandardDateTime_parsedCorrectly() throws TaskTrackerException {
        TaskDateTime dt = new TaskDateTime("2026-12-25 1800");
        assertEquals("Dec 25 2026, 6:00 PM", dt.toString());
    }

    @Test
    public void constructor_invalidFormat_exceptionThrown() {
        assertThrows(TaskTrackerException.class, () -> {
                     new TaskDateTime("invalid-date-format");
                     });
    }
}
