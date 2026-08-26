package tasktracker.parser;

import org.junit.jupiter.api.Test;
import tasktracker.task.Deadline;
import tasktracker.task.ToDo;
import tasktracker.exception.TaskTrackerException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    @Test
    public void parseToDo_validInput_returnsToDo() throws TaskTrackerException {
        ToDo todo = Parser.parseToDo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void parseToDo_emptyDescription_exceptionThrown() {
        assertThrows(TaskTrackerException.class, () -> {
                     Parser.parseToDo("   ");
                     });
    }
}
