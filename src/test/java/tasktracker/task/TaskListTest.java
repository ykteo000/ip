package tasktracker.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tasktracker.exception.TaskTrackerException;
import tasktracker.ui.Message;

public class TaskListTest {

    private TaskList taskList;
    private ToDo sampleTodo;
    private FixedDurationTask sampleFixedTask;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList(new ArrayList<>());
        sampleTodo = new ToDo("read documentation");
        sampleFixedTask = new FixedDurationTask("gym workout", "1 hour");
    }

    @Test
    public void add_validTasks_increasesSizeAndContainsTasks() throws TaskTrackerException {
        taskList.add(sampleTodo);
        assertEquals(1, taskList.getTasks().size());
        assertEquals(sampleTodo, taskList.getTasks().get(0));

        taskList.add(sampleFixedTask);
        assertEquals(2, taskList.getTasks().size());
        assertEquals(sampleFixedTask, taskList.getTasks().get(1));
    }

    @Test
    public void add_exceedsCapacity_throwsException() {
        // Populate taskList up to the 100-task limit
        for (int i = 0; i < 100; i++) {
            try {
                taskList.add(new ToDo("Task " + i));
            } catch (TaskTrackerException e) {
                // Should not throw during setup
            }
        }

        TaskTrackerException ex = assertThrows(TaskTrackerException.class, () -> {
            taskList.add(new ToDo("Task 101"));
        });
        assertEquals(Message.ERR_TASK_LIST_FULL, ex.getMessage());
    }

    @Test
    public void deleteTask_validOneBasedIndex_removesTaskAndDecrementsSize() throws TaskTrackerException {
        taskList.add(sampleTodo);
        taskList.add(sampleFixedTask);

        String resultMessage = taskList.deleteTask(1);
        assertTrue(resultMessage.contains(Message.MSG_TASK_REMOVED));
        assertEquals(1, taskList.getTasks().size());
        assertEquals(sampleFixedTask, taskList.getTasks().get(0));
    }

    @Test
    public void deleteTask_invalidIndex_throwsException() throws TaskTrackerException {
        taskList.add(sampleTodo);

        // Negative index
        assertThrows(TaskTrackerException.class, () -> {
            taskList.deleteTask(-1);
        });

        // Zero index (1-based system)
        assertThrows(TaskTrackerException.class, () -> {
            taskList.deleteTask(0);
        });

        // Out of bounds index
        assertThrows(TaskTrackerException.class, () -> {
            taskList.deleteTask(2);
        });
    }

    @Test
    public void setTaskStatus_markDone_updatesStatus() throws TaskTrackerException {
        taskList.add(sampleTodo);

        String result = taskList.setTaskStatus(1, true);
        assertTrue(result.contains(Message.MSG_TASK_MARKED));
        assertTrue(taskList.getTasks().get(0).toString().contains("[X]"));
    }

    @Test
    public void setTaskStatus_unmarkDone_updatesStatus() throws TaskTrackerException {
        taskList.add(sampleTodo);
        taskList.setTaskStatus(1, true);

        String result = taskList.setTaskStatus(1, false);
        assertTrue(result.contains(Message.MSG_TASK_UNMARKED));
        assertTrue(taskList.getTasks().get(0).toString().contains("[ ]"));
    }

    @Test
    public void findTasks_matchingKeyword_returnsFormattedResults() throws TaskTrackerException {
        taskList.add(new ToDo("read book chapter 1"));
        taskList.add(new ToDo("clean bedroom"));
        taskList.add(new ToDo("return library book"));

        String result = taskList.findTasks("book");
        assertTrue(result.contains("read book chapter 1"));
        assertTrue(result.contains("return library book"));
        assertTrue(!result.contains("clean bedroom"));
    }

    @Test
    public void findTasks_noMatch_returnsNoMatchingMessage() throws TaskTrackerException {
        taskList.add(sampleTodo);
        String result = taskList.findTasks("nonexistent");
        assertTrue(result.contains(Message.ERR_NO_MATCHING_TASKS));
    }

    @Test
    public void getFormattedList_emptyList_returnsEmptyMessage() {
        assertEquals(Message.ERR_TASK_LIST_EMPTY, taskList.getFormattedList());
    }

    @Test
    public void getFormattedList_populatedList_returnsNumberedItems() throws TaskTrackerException {
        taskList.add(sampleTodo);
        taskList.add(sampleFixedTask);

        String formatted = taskList.getFormattedList();
        assertTrue(formatted.startsWith("1. "));
        assertTrue(formatted.contains("2. "));
    }
}
