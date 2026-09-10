package tasktracker.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import tasktracker.exception.TaskTrackerException;
import tasktracker.task.Event;
import tasktracker.ui.Message;

public class ParserTest {

    // --- Happy Paths ---

    @Test
    public void parseEvent_validInput_success() throws TaskTrackerException {
        // Adjust date string format to match whatever your TaskDateTime accepts
        String input = "project meeting /from 2026-10-15 1400 /to 2026-10-15 1600";
        Event event = Parser.parseEvent(input);

        assertNotNull(event);
        assertEquals("project meeting", event.getDescription());
    }

    // --- Failure Modes & Edge Cases ---

    @Test
    public void parseEvent_emptyInput_throwsException() {
        assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("");
        });
        assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("   ");
        });
    }

    @Test
    public void parseEvent_missingFromDelimiter_throwsException() {
        TaskTrackerException ex = assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("project meeting /to 2026-10-15 1600");
        });
        assertEquals(Message.ERR_MISSING_FROM, ex.getMessage());
    }

    @Test
    public void parseEvent_missingToDelimiter_throwsException() {
        TaskTrackerException ex = assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("project meeting /from 2026-10-15 1400");
        });
        assertEquals(Message.ERR_MISSING_TO, ex.getMessage());
    }

    @Test
    public void parseEvent_invertedDelimiters_throwsException() {
        // /to appears before /from
        TaskTrackerException ex = assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("project meeting /to 2026-10-15 1600 /from 2026-10-15 1400");
        });
        assertEquals(Message.ERR_OUT_OF_ORDER, ex.getMessage());
    }

    @Test
    public void parseEvent_missingDescription_throwsException() {
        assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent(" /from 2026-10-15 1400 /to 2026-10-15 1600");
        });
    }

    @Test
    public void parseEvent_emptyTimes_throwsException() {
        // Empty /from or empty /to
        assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("meeting /from  /to 2026-10-15 1600");
        });
        assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("meeting /from 2026-10-15 1400 /to ");
        });
    }

    @Test
    public void parseEvent_endBeforeStartChronology_throwsException() {
        // End time is earlier than start time
        TaskTrackerException ex = assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("meeting /from 2026-10-15 1800 /to 2026-10-15 1400");
        });
        assertEquals(Message.ERR_EVENT_CHRONOLOGY, ex.getMessage());
    }

    @Test
    public void parseEvent_containsPipeCharacter_throwsException() {
        TaskTrackerException ex = assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("meet | team /from 2026-10-15 1400 /to 2026-10-15 1600");
        });
        assertEquals(Message.ERR_NO_DELIMITER, ex.getMessage());
    }

    @Test
    public void parseEvent_bothFromAndToMissingValues_throwsMissingFromException() {
        // Reproduces the exact bug you spotted: "event kirk anniversary /from /to"
        // It should reject /from first before complaining about /to
        TaskTrackerException ex = assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("kirk anniversary /from /to");
        });
        assertEquals(Message.ERR_MISSING_FROM, ex.getMessage());
    }

    @Test
    public void parseEvent_sameStartAndEndTime_success() throws TaskTrackerException {
        // Boundary case: event start and end at the exact same minute
        String input = "instant workshop /from 2026-10-15 1400 /to 2026-10-15 1400";
        Event event = Parser.parseEvent(input);

        assertNotNull(event);
        assertEquals("instant workshop", event.getDescription());
    }

    @Test
    public void parseEvent_pipeInDateFields_throwsException() {
        // Ensures '|' is rejected even if smuggled into the /from or /to section
        TaskTrackerException exFrom = assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("meeting /from 2026-10-15 | 1400 /to 2026-10-15 1600");
        });
        assertEquals(Message.ERR_NO_DELIMITER, exFrom.getMessage());

        TaskTrackerException exTo = assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("meeting /from 2026-10-15 1400 /to 2026-10-15 | 1600");
        });
        assertEquals(Message.ERR_NO_DELIMITER, exTo.getMessage());
    }

    @Test
    public void parseEvent_whitespacePadding_trimmedProperly() throws TaskTrackerException {
        // Ensures extra spaces around description and dates don't break parsing
        String input = "   project meeting    /from    2026-10-15 1400    /to    2026-10-15 1600   ";
        Event event = Parser.parseEvent(input);

        assertNotNull(event);
        assertEquals("project meeting", event.getDescription());
    }

    @Test
    public void parseEvent_delimitersWithoutSurroundingSpaces_treatedAsText() {
        // "project/from/to" should not be treated as tags since there are no spaces
        assertThrows(TaskTrackerException.class, () -> {
            Parser.parseEvent("project/from/to");
        });
    }
}
