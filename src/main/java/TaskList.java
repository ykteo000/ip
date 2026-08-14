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
	private final List<String> tasks;

	public TaskList() {
		this.tasks = new ArrayList<>();
	}

	public String add(String task) {
		if (tasks.size() >= MAX_TASKS) {
			return "Task list is full, remove any task first!";
		} else {
			tasks.add(task);
			return "added: " + task;
		}
	}

	public String getFormattedList() {
		if (tasks.isEmpty()) {
			return "No tasks added yet, please add a task first!";
		}

		return IntStream.range(0, tasks.size())
            			.mapToObj(i -> (i + 1) + ". " + tasks.get(i))
            			.collect(Collectors.joining("\n"));
	}
}

