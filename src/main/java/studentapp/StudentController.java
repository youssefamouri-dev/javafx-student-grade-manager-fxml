package studentapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/*
 * ============================================================================
 *  StudentController  -  the BEHAVIOR of the window
 * ============================================================================
 *
 *  This class is named in the FXML file:
 *      <VBox ... fx:controller="studentapp.StudentController">
 *
 *  When FXMLLoader reads the file, it:
 *    1. creates a StudentController object (it calls our empty constructor),
 *    2. fills every field marked @FXML whose name matches an fx:id in the FXML,
 *          <TextField fx:id="nameField"/>   ->   private TextField nameField;
 *    3. connects the events:
 *          <Button onAction="#handleAdd"/>  ->   private void handleAdd()
 *    4. calls initialize() at the end, once everything is ready.
 *
 *  Notice what is NOT here anymore compared to version 1:
 *    - no "new Button(...)", no "new HBox(...)"         -> it is in the FXML
 *    - no "setStyle(...)", no color codes               -> it is in the CSS
 *    - no "setOnAction(...)"                            -> it is in the FXML
 *  Only the logic is left.
 */
public class StudentController {

    // -----------------------------------------------------------------------
    //  1) The link with our normal Java code (unchanged from version 1).
    // -----------------------------------------------------------------------
    private final StudentManager manager = new StudentManager();

    // -----------------------------------------------------------------------
    //  2) The UI components. We do NOT create them: FXMLLoader injects them.
    //     @FXML lets FXMLLoader reach these private fields.
    //     The variable name MUST be the same as the fx:id in the FXML.
    // -----------------------------------------------------------------------
    @FXML private TextField nameField;
    @FXML private TextField gradeField;
    @FXML private ListView<Student> studentList;
    @FXML private Label statsLabel;
    @FXML private Label messageLabel;

    // -----------------------------------------------------------------------
    //  3) initialize() : called automatically by FXMLLoader after the @FXML
    //     fields are filled. (In the constructor, they would still be null!)
    // -----------------------------------------------------------------------
    @FXML
    private void initialize() {
        updateStatistics();
    }

    // -----------------------------------------------------------------------
    //  4) EVENT HANDLERS, connected in the FXML with onAction="#methodName".
    //     Flow:  user clicks -> read the UI -> call Java objects -> update the UI
    // -----------------------------------------------------------------------
    @FXML
    private void handleAdd() {
        String name = nameField.getText();
        String gradeText = gradeField.getText().trim();

        try {
            double grade = Double.parseDouble(gradeText);       // may throw NumberFormatException
            Student student = new Student(name, grade);         // may throw IllegalArgumentException

            manager.addStudent(student);                        // logic
            studentList.getItems().add(student);                // GUI (ObservableList refreshes itself)

            nameField.clear();
            gradeField.clear();
            nameField.requestFocus();
            showMessage("Added " + student.getName() + ".", false);
            updateStatistics();

        } catch (NumberFormatException e) {
            showMessage("The grade must be a number (example: 14.5).", true);
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage(), true);
        }
    }

    @FXML
    private void handleRemove() {
        Student selected = studentList.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Select a student in the list first.", true);
            return;
        }

        manager.removeStudent(selected);
        studentList.getItems().remove(selected);
        showMessage("Removed " + selected.getName() + ".", false);
        updateStatistics();
    }

    // -----------------------------------------------------------------------
    //  5) Helpers.
    // -----------------------------------------------------------------------
    private void updateStatistics() {
        if (manager.getCount() == 0) {
            statsLabel.setText("Students: 0   |   Average: -   |   Best: -");
            return;
        }
        statsLabel.setText(String.format("Students: %d   |   Average: %.2f / 20   |   Best: %s",
                manager.getCount(),
                manager.getAverage(),
                manager.getBestStudent().getName()));
    }

    // Version 1 wrote the colors here with setStyle("-fx-text-fill: #c0392b;").
    // Now Java only says WHICH KIND of message it is, by switching a CSS
    // "style class". The colors themselves live in style.css (.error / .success).
    private void showMessage(String text, boolean isError) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().removeAll("error", "success");
        messageLabel.getStyleClass().add(isError ? "error" : "success");
    }
}
