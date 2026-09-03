package tasktracker;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tasktracker.command.CommandType;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private TaskTracker taskTracker;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image taskTrackerImage = new Image(this.getClass().getResourceAsStream("/images/DaTaskTracker.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the TaskTracker instance and displays the welcome message */
    public void setTaskTracker(TaskTracker t) {
        this.taskTracker = t;
        dialogContainer.getChildren().add(
                DialogBox.getTaskTrackerDialog(taskTracker.getWelcomeMessage(), taskTrackerImage, null, false)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing TaskTracker's
     * reply and then appends them to the dialog container. Clears the input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = taskTracker.getResponse(input);

        CommandType commandType = taskTracker.getLastCommandType();
        boolean isError = taskTracker.isErrorResponse();

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getTaskTrackerDialog(response, taskTrackerImage, commandType, isError)
        );
        userInput.clear();
    }
}
