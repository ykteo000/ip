package tasktracker.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import tasktracker.exception.TaskTrackerException;
import tasktracker.task.ToDo;

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
