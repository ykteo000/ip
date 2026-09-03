package tasktracker.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tasktracker.exception.TaskTrackerException;
import tasktracker.ui.Message;

/**
 * Represents a collection of tasks and handles task-level operations such as adding,
 * formatting output, and updating task completion status.
 */
public class TaskList {
    // Set a limit to 100 to prevent user misuse.
    private static final int MAX_TASKS = 100;
    private final List<Task> taskList;

    /**
     * Initializes an empty TaskList.
     */
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Initializes a TaskList with pre-loaded tasks (from Storage).
     *
     * @param savedTasks List of tasks loaded from disk.
     */
    public TaskList(List<Task> savedTasks) {
        this.taskList = new ArrayList<>(savedTasks);
    }

    /**
     * Returns the underlying list of tasks for saving.
     *
     * @return List of current tasks.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(taskList);
    }

    /**
     * Adds a task to the list if space permits.
     *
     * @param task Task to be added.
     * @return Message confirming addition of the task.
     * @throws TaskTrackerException If the task list has reached MAX_TASKS capacity.
     */
    public String add(Task task) throws TaskTrackerException {
        if (taskList.size() >= MAX_TASKS) {
            throw new TaskTrackerException(Message.ERR_TASK_LIST_FULL);
        }

        taskList.add(task);
        return Message.MSG_TASK_ADDED + " " + task + "\n"
                + Message.getMsgTaskCount(taskList.size());
    }

    /**
     * Deletes a task from the list by its 1-based index.
     *
     * @param index 1-based index of the task to remove.
     * @return Confirmation message of the deleted task.
     * @throws TaskTrackerException If the task index is out of bounds.
     */
    public String deleteTask(int index) throws TaskTrackerException {
        if (index < 1 || index > taskList.size()) {
            throw new TaskTrackerException(Message.getErrOutOfBounds(taskList.size()));
        }

        Task removedTask = taskList.remove(index - 1);

        return Message.MSG_TASK_REMOVED + " " + removedTask + "\n"
                + Message.getMsgTaskCount(taskList.size());
    }

    /**
     * Finds and formats all tasks containing the specified keyword in their description.
     *
     * @param keyword The substring keyword to search for.
     * @return Formatted string containing all matching tasks, or a message indicating no matches.
     */
    public String findTasks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        List<Task> matchingTasks = taskList.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());

        if (matchingTasks.isEmpty()) {
            return Message.ERR_NO_MATCHING_TASKS + keyword + "\n";
        }

        return Message.MSG_FIND_MATCHING
                + IntStream.range(0, matchingTasks.size())
                .mapToObj(i -> (i + 1) + ". " + matchingTasks.get(i))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Generates a formatted string representing all tasks currently stored in the list.
     *
     * @return Formatted string of all tasks with 1-based indexing, else tells user list is empty.
     */
    public String getFormattedList() {
        if (taskList.isEmpty()) {
            return Message.ERR_TASK_LIST_EMPTY;
        }

        return IntStream.range(0, taskList.size())
                .mapToObj(i -> (i + 1) + ". " + taskList.get(i))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Sets the status of a task identified by its 1-based index.
     *
     * @param index  1-based task index.
     * @param isDone True to mark as done, false to mark as undone.
     * @return Confirmation message of the updated task status.
     * @throws TaskTrackerException If the task index is out of bounds.
     */
    public String setTaskStatus(int index, boolean isDone) throws TaskTrackerException {
        if (index < 1 || index > taskList.size()) {
            throw new TaskTrackerException(Message.getErrOutOfBounds(taskList.size()));
        }

        Task taskToUpdate = taskList.get(index - 1);

        if (isDone) {
            taskToUpdate.markAsDone();
            return Message.MSG_TASK_MARKED + " " + taskToUpdate;
        } else {
            taskToUpdate.markAsUndone();
            return Message.MSG_TASK_UNMARKED + " " + taskToUpdate;
        }
    }
}

