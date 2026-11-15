/*
 * module-info.java : tells Java which libraries (modules) our program needs.
 *
 *  - "requires javafx.controls"     -> we use JavaFX buttons, labels, text fields...
 *  - "requires javafx.fxml"         -> we load the UI from an .fxml file (FXMLLoader)
 *  - "exports studentapp"           -> JavaFX must be allowed to access our package,
 *                                      because JavaFX itself creates our StudentApp object.
 *  - "opens studentapp to javafx.fxml"
 *                                   -> FXMLLoader uses REFLECTION to create our controller
 *                                      and to fill its PRIVATE @FXML fields. "opens" gives
 *                                      it that permission. Forget it and you get an
 *                                      IllegalAccessException at startup!
 */
module studentapp {
    requires javafx.controls;
    requires javafx.fxml;

    exports studentapp;
    opens studentapp to javafx.fxml;
}
