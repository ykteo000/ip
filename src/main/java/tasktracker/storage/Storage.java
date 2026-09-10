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
    private static final String DELIMITER_REGEX = " \\| ";
    private static final String STATUS_DONE = "1";
    private static final String STATUS_NOT_DONE = "0";
    private static final String TYPE_TODO = "T";
    private static final String TYPE_DEADLINE = "D";
    private static final String TYPE_EVENT = "E";
    private static final String TEMP_FILE_PREFIX = "tasks_";
    private static final String TEMP_FILE_SUFFIX = ".tmp";

    private final String filePath;

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
     *
     * @param tasks The list of tasks to be saved.
     * @throws TaskTrackerException If an I/O error occurs while writing to the file.
     */
    public void save(List<Task> tasks) throws TaskTrackerException {
        Path targetPath = Paths.get(filePath);
        Path parentDir = targetPath.getParent();

        try {
            ensureDirectoryExists(parentDir);
            Path tempPath = writeTasksToTempFile(parentDir, tasks);
            replaceTargetFile(tempPath, targetPath);
        } catch (IOException e) {
            throw new TaskTrackerException(Message.ERR_FILE_SAVE + e.getMessage());
        }
    }

    private void ensureDirectoryExists(Path parentDir) throws IOException {
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
    }

    private Path writeTasksToTempFile(Path parentDir, List<Task> tasks) throws IOException {
        Path tempPath = (parentDir != null)
                ? Files.createTempFile(parentDir, TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX)
                : Files.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);

        try (BufferedWriter writer = Files.newBufferedWriter(tempPath)) {
            for (Task task : tasks) {
                writer.write(task.toFileFormat());
                writer.newLine();
            }
        }
        return tempPath;
    }

    private void replaceTargetFile(Path source, Path target) throws IOException {
        try {
            Files.move(source, target,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Loads tasks from the storage file upon application startup.
     *
     * @return A list of tasks parsed from the file, or an empty list if no save file exists.
     * @throws TaskTrackerException If the file contains invalid formatting or an I/O error occurs.
     */
    public List<Task> load() throws TaskTrackerException {
        List<Task> loadedTasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return loadedTasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                loadedTasks.add(parseTaskFromLine(line));
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
        String[] parts = line.split(DELIMITER_REGEX);
        if (parts.length < 3) {
            throw new TaskTrackerException(Message.ERR_FILE_CORRUPT + line);
        }

        String type = parts[0].trim();
        String status = parts[1].trim();
        String description = parts[2].trim();

        boolean isDone = parseTaskStatus(status, line);
        Task task = instantiateTask(type, description, parts, line);

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    private boolean parseTaskStatus(String status, String line) throws TaskTrackerException {
        if (!status.equals(STATUS_NOT_DONE) && !status.equals(STATUS_DONE)) {
            throw new TaskTrackerException(Message.ERR_FILE_CORRUPT + line);
        }
        return status.equals(STATUS_DONE);
    }

    private Task instantiateTask(String type, String description, String[] parts, String line)
            throws TaskTrackerException {
        switch (type) {
            case TYPE_TODO -> {
                return new ToDo(description);
            }
            case TYPE_DEADLINE -> {
                return createDeadline(description, parts, line);
            }
            case TYPE_EVENT -> {
                return createEvent(description, parts, line);
            }
            default -> throw new TaskTrackerException(Message.ERR_FILE_UNKNOWN + type);
        }
    }

    private Deadline createDeadline(String description, String[] parts, String line)
            throws TaskTrackerException {
        if (parts.length < 4) {
            throw new TaskTrackerException(Message.ERR_FILE_DEADLINE + line);
        }
        try {
            return new Deadline(description, new TaskDateTime(parts[3].trim()));
        } catch (TaskTrackerException e) {
            throw new TaskTrackerException(Message.ERR_FILE_CORRUPT + line);
        }
    }

    private Event createEvent(String description, String[] parts, String line)
            throws TaskTrackerException {
        if (parts.length < 5) {
            throw new TaskTrackerException(Message.ERR_FILE_EVENT + line);
        }
        try {
            return new Event(description, new TaskDateTime(parts[3].trim()),
                    new TaskDateTime(parts[4].trim()));
        } catch (TaskTrackerException e) {
            throw new TaskTrackerException(Message.ERR_FILE_CORRUPT + line);
        }
    }
}
