/**
 * Represents a ToDo task without any date or time constraints.
 */
public class ToDo extends Task {
	/**
     	* Constructs a ToDo instance with the specified task description.
     	*
     	* @param description Text describing the todo task.
     	*/
	public ToDo(String description) {
		super(description);
	}

	@Override
	public String toString() {
		return "[T]" + super.toString();
	}
}

