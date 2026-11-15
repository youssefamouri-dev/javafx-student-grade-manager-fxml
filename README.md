# Student Grade Manager (JavaFX + FXML + CSS)

A small JavaFX desktop app for learning. You type a student's name and a grade (0 to 20) and click **Add**. The student appears in a list, and the app shows the student count, the class average and the best student. You can also remove the selected student.

The project shows how to split a JavaFX GUI into three files, each with one job:

| File | Job | Language |
|------|-----|----------|
| `student-view.fxml` | **What** is in the window (structure) | XML |
| `style.css` | **How** it looks (colors, fonts, spacing) | CSS |
| `StudentController.java` | **What happens** on a click (behavior) | Java |

> 📘 **Want to build it yourself?** See the step-by-step exercise in [`docs/PROBLEM_STATEMENT.md`](docs/PROBLEM_STATEMENT.md).

---

## Table of contents

1. [Project structure](#project-structure)
2. [Prerequisites](#prerequisites)
3. [Get the code](#get-the-code)
4. [Run from the command line](#run-from-the-command-line)
5. [Setup in VS Code](#setup-in-vs-code)
6. [Setup in Eclipse](#setup-in-eclipse)
7. [How the pieces connect](#how-the-pieces-connect)
8. [Common pitfalls](#common-pitfalls)
9. [Troubleshooting](#troubleshooting)

---

## Project structure

```
.
├── build.gradle                       Gradle config: downloads javafx.controls + javafx.fxml
├── settings.gradle
├── gradlew / gradlew.bat              Gradle wrapper (no need to install Gradle)
├── gradle/wrapper/
├── docs/
│   └── PROBLEM_STATEMENT.md           the exercise: build this project step by step
└── src/main/
    ├── java/
    │   ├── module-info.java           requires javafx.fxml + "opens studentapp to javafx.fxml"
    │   └── studentapp/
    │       ├── Person.java            plain OOP class (parent)
    │       ├── Student.java           extends Person, adds a grade
    │       ├── StudentManager.java    stores students, computes average and best student
    │       ├── StudentApp.java        entry point: loads the FXML, shows the window
    │       └── StudentController.java @FXML fields + click handlers
    └── resources/studentapp/
        ├── student-view.fxml          the scene graph, written in XML
        └── style.css                  the look
```

`Person`, `Student` and `StudentManager` contain **no JavaFX code**. They are the model. The GUI only calls them.

---

## Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| **JDK** | **17 or newer** (21 LTS recommended) | For example [Eclipse Temurin](https://adoptium.net/). The code compiles with `release 17`. |
| **Git** | any | To clone the repository. |
| **Gradle** | *not needed* | The project ships with the Gradle wrapper (`gradlew`), which downloads Gradle 9.8.1 on the first run. |
| **JavaFX** | *not needed* (when using Gradle) | Since Java 11, JavaFX is **not part of the JDK**. The `org.openjfx.javafxplugin` Gradle plugin downloads JavaFX **21.0.5** for your OS automatically. |
| Internet connection | first build only | To download Gradle, the JavaFX plugin and the JavaFX jars. |
| [Scene Builder](https://gluonhq.com/products/scene-builder/) | optional | Drag-and-drop editor for `.fxml` files. |

Check your JDK:

```bash
java -version     # should print 17 or higher
```

If several JDKs are installed, make sure `JAVA_HOME` points to a JDK 17+.

---

## Get the code

```bash
git clone https://github.com/youssefamouri-dev/javafx-student-grade-manager-fxml.git
cd javafx-student-grade-manager-fxml
```

---

## Run from the command line

This works on any OS and is the fastest way to check your setup:

```bash
# Windows
gradlew.bat run

# macOS / Linux
./gradlew run
```

Other useful tasks:

```bash
./gradlew build      # compile + package into build/libs/
./gradlew clean      # delete the build/ folder
```

---

## Setup in VS Code

### 1. Install the extensions

| Extension | ID | Why |
|-----------|----|-----|
| **Extension Pack for Java** (Microsoft) | `vscjava.vscode-java-pack` | Java language support, debugger, test runner, Maven, project manager. |
| **Gradle for Java** (Microsoft) | `vscjava.vscode-gradle` | Imports the Gradle project and adds the Gradle side panel (elephant icon). Recent versions of the Java pack include it. If yours doesn't, install it separately. |
| *(optional)* An XML extension, for example **XML** by Red Hat | `redhat.vscode-xml` | Syntax checking and completion in `.fxml` files. |

You can install them from the Extensions view (`Ctrl+Shift+X`) or from a terminal:

```bash
code --install-extension vscjava.vscode-java-pack
code --install-extension vscjava.vscode-gradle
```

### 2. Point VS Code to a JDK 17+

If `java -version` already prints 17+, there is usually nothing to do. Otherwise open **Command Palette** (`Ctrl+Shift+P`) and choose **Java: Configure Java Runtime**, then pick a JDK 17+.

### 3. Open the project

`File > Open Folder…` and select the cloned folder. The Java extension detects `build.gradle` and imports the project. Wait until the status bar says the Java project is ready. The first import downloads JavaFX and can take a minute.

### 4. How JavaFX is set up in VS Code

You **don't download JavaFX by hand**. Gradle does it:

```groovy
plugins {
    id 'application'
    id 'org.openjfx.javafxplugin' version '0.1.0'
}

javafx {
    version = '21.0.5'
    modules = ['javafx.controls', 'javafx.fxml']
}
```

The repository includes `.vscode/settings.json` with:

```json
"java.gradle.buildServer.enabled": "off"
```

This tells VS Code to import the project with the classic Gradle importer (Buildship) instead of the newer "Gradle Build Server". The Build Server does not put the JavaFX jars on the **module path**, so with our `module-info.java` the editor would show *"javafx.controls cannot be resolved to a module"* and every JavaFX type would be red. Keep this setting.

> If you changed this setting after the project was already imported, run **Command Palette > Java: Clean Java Language Server Workspace** and restart VS Code.

### 5. Run the app

Use one of these:

- **Gradle panel:** click the elephant icon in the left bar, then `student-grade-manager-fxml > Tasks > application > run`.
- **Terminal** (`` Ctrl+` ``): `gradlew.bat run` on Windows, or `./gradlew run`.

> ⚠️ Clicking **Run** above `main()` in `StudentApp.java` may fail with *"JavaFX runtime components are missing"*, because it can start the app without the JavaFX module path. Use the Gradle `run` task instead.

---

## Setup in Eclipse

### 1. Install Eclipse and the plugins

| Item | Why |
|------|-----|
| **Eclipse IDE for Java Developers** (2023-09 or newer) | Recent versions support Java 17/21. This package already includes **Buildship**, Eclipse's Gradle integration. |
| **Buildship Gradle Integration** | Only if your Eclipse package doesn't have it: `Help > Eclipse Marketplace…`, search *Buildship*, install. |
| *(optional)* **e(fx)clipse** | `Help > Eclipse Marketplace…`, search *e(fx)clipse*. Adds an FXML editor, JavaFX CSS completion and a "Open with Scene Builder" menu. Not required to build or run. |

### 2. Point Eclipse to a JDK 17+

`Window > Preferences > Java > Installed JREs > Add… > Standard VM`, select your JDK folder (for example `C:\Program Files\Eclipse Adoptium\jdk-21…`), and check it as the default.

Then `Window > Preferences > Gradle` and, if needed, set **Java home** to the same JDK.

### 3. Option A (recommended): import as a Gradle project

With this option, Gradle downloads JavaFX for you, the same way as in VS Code.

1. `File > Import… > Gradle > Existing Gradle Project > Next`.
2. **Project root directory:** select the cloned folder. Click **Finish**.
3. Wait for the import to finish (watch the progress bar at the bottom right). Buildship reads `build.gradle`, downloads JavaFX 21.0.5 and puts it on the **module path**, because the project has a `module-info.java`.
4. Run the app:
   - Open the view `Window > Show View > Other… > Gradle > Gradle Tasks`.
   - Expand `student-grade-manager-fxml > application` and **double-click `run`**.

> If you edit `build.gradle`, right-click the project then `Gradle > Refresh Gradle Project`.

> ⚠️ `Run As > Java Application` on `StudentApp.java` can fail with *"JavaFX runtime components are missing"*. Use the Gradle `run` task, or use Option B below.

### 4. Option B: plain Java project with a manually downloaded JavaFX SDK

Use this if you can't use Gradle, or if you want to see what Gradle does for you.

1. **Download the JavaFX SDK** for your OS from [gluonhq.com/products/javafx](https://gluonhq.com/products/javafx/). Pick version **21** (LTS), type **SDK**. Unzip it, for example to `C:\javafx-sdk-21`. The jars are in `C:\javafx-sdk-21\lib`.
2. **Create a User Library:**
   `Window > Preferences > Java > Build Path > User Libraries > New…`, name it `JavaFX21`.
   Select it, then **Add External JARs…** and add every `.jar` from `C:\javafx-sdk-21\lib`.
3. **Create the project:** `File > New > Java Project`. Uncheck *Use default location* and point it to the cloned folder, or create a new project and copy `src/main/java` and `src/main/resources` into it. Choose JRE 17+.
4. **Configure the build path:** right-click the project, then `Build Path > Configure Build Path…`:
   - **Source** tab: make sure both `src/main/java` **and** `src/main/resources` are source folders. The FXML and CSS files must be copied to the output next to the classes.
   - **Libraries** tab: select **Modulepath**, then `Add Library… > User Library > JavaFX21`. Use **Modulepath**, not Classpath, because the project has a `module-info.java`.
5. **Create a Run Configuration:** `Run > Run Configurations… > Java Application > New`:
   - **Main class:** `studentapp.StudentApp`
   - **Arguments > VM arguments:**
     ```
     --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml
     ```
     (On macOS/Linux use your path, for example `--module-path /Users/you/javafx-sdk-21/lib`.)
   - On macOS, uncheck **Use the -XstartOnFirstThread argument** if it is shown.
6. Click **Run**.

---

## How the pieces connect

```
StudentApp.start()
   └─ FXMLLoader.load("student-view.fxml")
         ├─ creates every node:  <VBox>, <Label>, <TextField>, <Button> ...
         ├─ reads stylesheets="@style.css"            -> applies the CSS
         ├─ reads fx:controller="studentapp.StudentController"
         │     └─ new StudentController()
         ├─ fx:id="nameField"     ->  @FXML private TextField nameField;
         ├─ onAction="#handleAdd" ->  @FXML private void handleAdd()
         └─ calls controller.initialize()
```

**Why FXML + CSS?**
- **Separation of concerns:** you can change the look (CSS) or the layout (FXML) without touching the Java logic. It's the same idea as HTML + CSS + JS on the web.
- **Scene Builder:** you can open the `.fxml` file and edit it by drag and drop.
- **Shorter Java:** `StudentApp` is only about 10 real lines.
- **Theming:** you can change the colors of the whole app from the variables at the top of `style.css`.

---

## Common pitfalls

1. **Forgetting `opens studentapp to javafx.fxml`** in `module-info.java` causes an `IllegalAccessException` at startup. FXMLLoader uses reflection to fill the private `@FXML` fields, so it needs that permission.
2. **Wrong `fx:id`:** if the field name in the controller doesn't match the `fx:id`, the field stays `null` and you get a `NullPointerException` on the first click.
3. **Using `@FXML` fields in the constructor:** they are still `null` at that point. Use `initialize()` instead.
4. **Resource path:** the FXML and CSS files are in `src/main/resources/studentapp/`, the same package as `StudentApp`, so `StudentApp.class.getResource("student-view.fxml")` finds them.
5. **CSS property names** start with `-fx-` (`-fx-text-fill`, not `color`).
6. **`--` inside an XML comment** is not allowed and makes FXMLLoader fail with a parse error.

---

## Troubleshooting

| Error | Cause / fix |
|-------|-------------|
| `Error: JavaFX runtime components are missing` | The app was started without JavaFX on the module path. Run it with `gradlew run`, or add the `--module-path … --add-modules …` VM arguments (Eclipse Option B). |
| `javafx.controls cannot be resolved to a module` (VS Code) | Check that `.vscode/settings.json` contains `"java.gradle.buildServer.enabled": "off"`, then run **Java: Clean Java Language Server Workspace**. |
| `Location is not set` / `NullPointerException` in `FXMLLoader` | `student-view.fxml` was not found. Check that it is in `src/main/resources/studentapp/` and that `src/main/resources` is a source folder (Eclipse Option B). |
| `IllegalAccessException … cannot access class studentapp.StudentController` | Add `opens studentapp to javafx.fxml;` to `module-info.java`. |
| `Unsupported class file major version` / `invalid source release` | Your JDK is too old. Use JDK 17+ and make sure the IDE and Gradle use it (`JAVA_HOME`, *Installed JREs*, *Java: Configure Java Runtime*). |
| `./gradlew: Permission denied` (macOS/Linux) | `chmod +x gradlew` |
