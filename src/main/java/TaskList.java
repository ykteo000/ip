import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Stores user input to a list as an ArrayList<String>.
 *
 * Array stores a maximum of 100 elements.
 * Provides operations to view and edit elements.
 * 
 * @author Yong Kang Teo
 * @version 1.0
 */
public class TaskList {
	private final List<String> tasks;

	public TaskList() {
		this.tasks = new ArrayList<>();
	}

	public void add(String task) {
		tasks.add(task);
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

