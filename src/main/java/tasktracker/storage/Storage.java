package tasktracker.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tasktracker.exception.TaskTrackerException;
import tasktracker.task.Deadline;
import tasktracker.task.Event;
import tasktracker.task.Task;
import tasktracker.task.TaskDateTime;
import tasktracker.task.ToDo;
import tasktracker.ui.Message;

/**
 * Handles the loading and saving of task data to and from a local file.
 * <p>
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
     * Saves the provided list of tasks to the storage file atomically.
     * <p>
     * Writes to a temporary file first and replaces the existing file upon success.
     * Creates any missing parent directories before writing.
     *
     * @param tasks The list of tasks to be saved.
     * @throws TaskTrackerException If an I/O error occurs while writing to the file.
     */
    public void save(List<Task> tasks) throws TaskTrackerException {
        Path targetPath = Paths.get(filePath);
        Path parentDir = targetPath.getParent();

        try {
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            // Create a temp file in the same directory so the move operation stays on the same filesystem
            Path tempPath = (parentDir != null)
                    ? Files.createTempFile(parentDir, "tasks_", ".tmp")
                    : Files.createTempFile("tasks_", ".tmp");

            try (BufferedWriter writer = Files.newBufferedWriter(tempPath)) {
                for (Task task : tasks) {
                    writer.write(task.toFileFormat());
                    writer.newLine();
                }
            }

            // Atomically replace the destination file
            try {
                Files.move(tempPath, targetPath,
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                // Fallback for file systems or OS environments that do not support atomic moves
                Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new TaskTrackerException(Message.ERR_FILE_SAVE + e.getMessage());
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
            throw new TaskTrackerException(Message.ERR_FILE_LOAD + e.getMessage());
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
            throw new TaskTrackerException(Message.ERR_FILE_CORRUPT + line);
        }

        String type = parts[0].trim();
        String status = parts[1].trim();
        if (!status.equals("0") && !status.equals("1")) {
            throw new TaskTrackerException(Message.ERR_FILE_CORRUPT + line);
        }
        boolean isDone = status.equals("1");
        String description = parts[2].trim();

        Task task;
        switch (type) {
            case "T":
                task = new ToDo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    throw new TaskTrackerException(Message.ERR_FILE_DEADLINE + line);
                }
                try {
                    task = new Deadline(description, new TaskDateTime(parts[3].trim()));
                } catch (TaskTrackerException e) {
                    throw new TaskTrackerException(Message.ERR_FILE_CORRUPT + line);
                }
                break;
            case "E":
                if (parts.length < 5) {
                    throw new TaskTrackerException(Message.ERR_FILE_EVENT + line);
                }
                try {
                    task = new Event(description, new TaskDateTime(parts[3].trim()),
                            new TaskDateTime(parts[4].trim()));
                } catch (TaskTrackerException e) {
                    throw new TaskTrackerException(Message.ERR_FILE_CORRUPT + line);
                }
                break;
            default:
                throw new TaskTrackerException(Message.ERR_FILE_UNKNOWN + type);
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
