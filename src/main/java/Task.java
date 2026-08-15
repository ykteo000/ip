/**
 * Represents a generic task in the application.
 * Serves as the base class for specific task types like ToDo, Deadline, and Event.
 */ 
// Credits: Skeleton class template provided from CS2103T website.
public class Task {
	protected String description;
    	protected boolean isDone;

    	/**
     	* Constructs a Task instance with specified description and sets completion status to false.
     	*
     	* @param description Text describing the task.
     	*/
	public Task(String description) {
        	this.description = description;
        	this.isDone = false;
    	}

    	/**
     	* Gets the completion status icon representing whether the task is done.
     	*
     	* @return String "X" if done, or a single space if undone.
     	*/
	public String getStatusIcon() {
        	return (isDone ? "X" : " "); // mark done task with X
    	}	

	/**
     	* Marks this task as completed.
     	*/
	public void markAsDone() {
		this.isDone = true;
	}

	/**
     	* Marks this task as uncompleted.
     	*/
	public void markAsUndone() {
		this.isDone = false;
	}

	@Override
	public String toString() {
		return "[" + getStatusIcon() + "] " + this.description;
	}
}
