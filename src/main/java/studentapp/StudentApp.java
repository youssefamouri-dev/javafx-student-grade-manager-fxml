package studentapp;

import java.io.IOException;

import javafx.application.Application;   // the base class of every JavaFX app
import javafx.fxml.FXMLLoader;           // reads an .fxml file and builds the nodes
import javafx.scene.Parent;              // the common parent type of every layout
import javafx.scene.Scene;               // the content of the window
import javafx.stage.Stage;               // the window itself

/*
 * ============================================================================
 *  StudentApp  -  the ENTRY POINT of the application (FXML version)
 * ============================================================================
 *
 *  In the first version, this class did EVERYTHING: create the buttons,
 *  place them in HBox/VBox, style them, and handle the clicks.
 *
 *  Now the work is split into 3 files, each with ONE job:
 *
 *      student-view.fxml      -> WHAT the window contains   (structure, XML)
 *      style.css              -> HOW it looks               (colors, fonts, spacing)
 *      StudentController.java -> WHAT HAPPENS on a click    (behavior, Java)
 *
 *  This class only has to: load the FXML, put it in a Scene, show the Stage.
 *
 *  The theater metaphor is still the same:
 *      Stage = the window, Scene = the decor, Nodes = the actors.
 *  The only difference: the actors are now described in XML instead of Java.
 */
public class StudentApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // 1) Load the FXML file.
        //    getResource() looks for the file next to this class, in the same
        //    package (src/main/resources/studentapp/student-view.fxml).
        //    FXMLLoader reads the XML, creates every node (VBox, Label, Button...),
        //    creates the controller written in fx:controller, and connects them.
        FXMLLoader loader = new FXMLLoader(StudentApp.class.getResource("student-view.fxml"));
        Parent root = loader.load();

        // 2) Put the root node in a Scene.
        //    The CSS file is already attached inside the FXML (stylesheets="@style.css").
        //    We could also do it here with:
        //        scene.getStylesheets().add(StudentApp.class.getResource("style.css").toExternalForm());
        Scene scene = new Scene(root, 560, 520);

        // 3) Put the scene on the stage and show the window.
        stage.setTitle("JavaFX Demo - Student Grade Manager (FXML + CSS)");
        stage.setScene(scene);
        stage.show();
    }

    // main() : launch() starts the JavaFX engine, creates a StudentApp object
    // and calls start() for us.
    public static void main(String[] args) {
        launch(args);
    }
}
