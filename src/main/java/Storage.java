import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the loading and saving of task data to and from a local file.
 *
 * Note: Gemini AI was used here, especially for the save and load methods.
 * Prompt "I want to save all the user input and list as a log file."
 * Prompt "This is one sample format for the file, which we can follow."
 * Prompt "How do we convert the saved file back when loading?"
 * Add code only after an intermediate level of understanding achieved.
 */
public class Storage {
	private static final String DEFAULT_FILE_PATH = "./data/tasks.txt";
	private final String filePath;

	/**
	 * Constructs a Storage instance with the default file path.
	 */
	public Storage() {
		this(DEFAULT_FILE_PATH);
	}

	/**
	 * Constructs a Storage instance with the specified file path.
	 *
	 * @param filePath The path where tasks are saved.
	 */
	public Storage(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Saves the provided list of tasks to the storage file.
	 * Creates any missing parent directories before writing.
	 *
	 * @param tasks The list of tasks to be saved.
	 * @throws TaskTrackerException If an I/O error occurs while writing to the file.
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
	 * Loads tasks from the storage file upon application startup.
	 *
	 * @return A list of tasks parsed from the file, or an empty list if no save file exists.
	 * @throws TaskTrackerException If the file contains invalid formatting or I/O error occurs.
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
	 * Converts a single line from the save file into a corresponding {@code Task} object.
	 *
	 * @param line A single pipe-delimited line from the storage file.
	 * @return The instantiated {@code Task} object with its completion status updated.
	 * @throws TaskTrackerException If the task type is unrecognized or fields are missing.
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
