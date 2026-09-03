package tasktracker;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import tasktracker.command.CommandType;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    private void changeDialogStyle(CommandType commandType, boolean isError) {
        if (isError) {
            dialog.getStyleClass().add("error-label");
            return;
        }

        if (commandType == null) {
            dialog.getStyleClass().add("info-label");
            return;
        }

        switch (commandType) {
            case TODO:
            case DEADLINE:
            case EVENT:
                dialog.getStyleClass().add("add-label");
                break;
            case MARK:
                dialog.getStyleClass().add("marked-label");
                break;
            case UNMARK:
                dialog.getStyleClass().add("unmarked-label");
                break;
            case DELETE:
                dialog.getStyleClass().add("delete-label");
                break;
            case LIST:
            case FIND:
            case HELP:
            case BYE:
            default:
                dialog.getStyleClass().add("info-label");
                break;
        }
    }

    public static DialogBox getTaskTrackerDialog(String text, Image img, CommandType commandType, boolean isError) {
        var db = new DialogBox(text, img);
        db.flip();
        db.changeDialogStyle(commandType, isError);
        return db;
    }
}
