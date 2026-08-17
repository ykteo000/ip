import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
	private static final String DEFAULT_FILE_PATH = "./data/tasks.txt";
	private final String filePath;

	public Storage() {
		this(DEFAULT_FILE_PATH);
	}

	public Storage(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Saves the list of tasks to the hard drive file.
	 */
	public void save(List<Task> tasks) throws TaskTrackerException {
		File file = new File(filePath);
		if (file.getParentFile() != null) {
			file.getParentFile().mkdirs();
		}

		try (FileWriter writer = new FileWriter(file)) {
			for (Task task : tasks) {
				writer.write(task.toFileFormat() + System.lineSeparator());
			}
		} catch (IOException e) {
			throw new TaskTrackerException("Failed to save tasks: " + e.getMessage());
		}
	}


	/**
	 * Loads tasks from the hard drive file upon app startup.
	 */
	public List<Task> load() throws TaskTrackerException {
		List<Task> loadedTasks = new ArrayList<>();
		File file = new File(filePath);

		if (!file.exists()) {
			return loadedTasks; // Return empty list if no save file exists yet
		}

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) {
					continue;
				}
				Task task = parseTaskFromLine(line);
				if (task != null) {
					loadedTasks.add(task);
				}
			}
		} catch (IOException e) {
			throw new TaskTrackerException("Failed to load tasks: " + e.getMessage());
		}
		return loadedTasks;

	}

	/**
	 * Helper method to convert a saved file line back into a Task object.
	 */
	private Task parseTaskFromLine(String line) throws TaskTrackerException {
		String[] parts = line.split(" \\| ");
		if (parts.length < 3) {
			throw new TaskTrackerException("Corrupted file entry: " + line);
		}

		String type = parts[0];
		boolean isDone = parts[1].equals("1");
		String description = parts[2];

		Task task;
		switch (type) {
			case "T":
				task = new ToDo(description);
				break;
			case "D":
				if (parts.length < 4) {
					throw new TaskTrackerException("Corrupted Deadline entry: " + line);
				}
				task = new Deadline(description, parts[3]);
				break;
			case "E":
				if (parts.length < 5) {
					throw new TaskTrackerException("Corrupted Event entry: " + line);
				}
				task = new Event(description, parts[3], parts[4]);
				break;
			default:
				throw new TaskTrackerException("Unknown task type in file: " + type);
		}

		if (isDone) {
			task.markAsDone();
		}

		return task;
	}
}
