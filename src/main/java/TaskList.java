import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
     	* Adds a task to the list if space permits.
     	*
     	* @param task Task to be added.
     	* @return Message confirming addition or warning that list is full.
     	*/
	public String add(Task task) {
		if (taskList.size() >= MAX_TASKS) {
			return "Task list is full, remove any task first!";
		} else {
			taskList.add(task);
			return "added: " + task + "\nNow you have "
					+ taskList.size() + " tasks in the list!";
		}
	}

	/**
     	* Generates a formatted string representing all tasks currently stored in the list.
     	*
     	* @return Formatted string of all tasks with 1-based indexing.
     	*/
	public String getFormattedList() {
		if (taskList.isEmpty()) {
			return "No tasks added yet, please add a task first!";
		}

		return IntStream.range(0, taskList.size())
            			.mapToObj(i -> (i + 1) + ". " + taskList.get(i))
            			.collect(Collectors.joining("\n"));
	}

	/**
     	* Sets the status of a task identified by its string index.
     	*
     	* @param argument 1-based task index string entered by user.
     	* @param isDone True to mark as done, false to mark as undone.
     	* @return Confirmation message or error description if index is invalid.
     	*/
	public String setTaskStatus(String argument, boolean isDone) {
		// Check if argument is empty or " "
		if (argument.isEmpty() || argument.contains(" ")) {
			return "Please provide a valid single task index! ty :>";
		}
		
		// Convert argument to an int index
		try {
			int index = Integer.parseInt(argument);
			int zeroBasedIndex = index - 1;

			// Check if index is out of bounds
			if (zeroBasedIndex < 0 || zeroBasedIndex >= taskList.size()) {
				return "Invalid task number! Check your index!";
			}

			Task taskToUpdate = taskList.get(zeroBasedIndex);

			// Perform action based off boolean flag
			if (isDone) {
				taskToUpdate.markAsDone();
				return "Well done on completing this task! Marked as done! :D\n"
						+ taskToUpdate;
			} else {
				taskToUpdate.markAsUndone();
				return "Awwh... marked this as undone. Try finish soon ya? :P\n"
						+ taskToUpdate;
			}
		} catch (NumberFormatException e) {
			return "Please provide a valid numeric task number!";
		}
	}

}

