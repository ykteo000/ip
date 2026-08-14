import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Stores user input to a list as an ArrayList<String>.
 *
 * Provides operations to view and edit elements.
 * 
 * @author Yong Kang Teo
 * @version 1.0
 */
public class TaskList {
	private static final int MAX_TASKS = 100;
	private final List<Task> tasksList;

	public TaskList() {
		this.tasksList = new ArrayList<>();
	}

	public String add(String description) {
		if (tasksList.size() >= MAX_TASKS) {
			return "Task list is full, remove any task first!";
		} else {
			Task newTask = new Task(description);
			tasksList.add(newTask);
			return "added: " + description;
		}
	}

	public String getFormattedList() {
		if (tasksList.isEmpty()) {
			return "No tasks added yet, please add a task first!";
		}

		return IntStream.range(0, tasksList.size())
            			.mapToObj(i -> (i + 1) + ". " + tasksList.get(i))
            			.collect(Collectors.joining("\n"));
	}

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
			if (zeroBasedIndex < 0 || zeroBasedIndex >= tasksList.size()) {
				return "Invalid task number! Check your index!";
			}

			Task taskToUpdate = tasksList.get(zeroBasedIndex);

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

