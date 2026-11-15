# Exercise: Student Grade Manager with JavaFX, FXML and CSS

## Problem statement

A teacher wants a small desktop application to enter the grades of a class and see some statistics right away.

The application has **one window** where the user can:

1. type a **student name** and a **grade** (from 0 to 20), then click **Add** or press **ENTER** in the grade field;
2. see all the students in a **list**. Each line shows the name, the grade and whether the student **Passed** (grade ≥ 10) or **Failed**;
3. select a student in the list and click **Remove selected** to delete them;
4. see a **statistics line** that is always up to date: the number of students, the class average (2 decimals) and the name of the best student;
5. see a **feedback message**, green for success (*"Added Sara."*) and red for errors (*"The grade must be a number (example: 14.5)."*).

The application must follow these **design rules**:

- The **model** (`Person`, `Student`, `StudentManager`) is plain Java and **must not import anything from JavaFX**.
- The **structure** of the window is described in an **FXML** file, not in Java code.
- The **look** of the window (colors, fonts, spacing) is described in a **CSS** file. No `setStyle(...)` in Java.
- The **behavior** (what happens on a click) is in a **controller** class.

### Expected result

```
┌──────────────────────────────────────────────────────────┐
│ Student Grade Manager                                    │
│ Built with FXML + CSS                                    │
│ ┌──────────────────────────┐ ┌──────────────┐ ┌─────┐    │
│ │ Student name             │ │ Grade (0-20) │ │ Add │    │
│ └──────────────────────────┘ └──────────────┘ └─────┘    │
│ ┌──────────────────────────────────────────────────────┐ │
│ │ Sara  -  15.5 / 20  (Passed)                         │ │
│ │ Adam  -  8.0 / 20  (Failed)                          │ │
│ │                                                      │ │
│ └──────────────────────────────────────────────────────┘ │
│ ┌─────────────────┐                                      │
│ │ Remove selected │                                      │
│ └─────────────────┘                                      │
│ Students: 2   |   Average: 11.75 / 20   |   Best: Sara   │
│ Added Adam.                                              │
└──────────────────────────────────────────────────────────┘
```

### Final project structure

```
.
├── build.gradle
├── settings.gradle
└── src/main/
    ├── java/
    │   ├── module-info.java
    │   └── studentapp/
    │       ├── Person.java
    │       ├── Student.java
    │       ├── StudentManager.java
    │       ├── StudentApp.java
    │       └── StudentController.java
    └── resources/studentapp/
        ├── student-view.fxml
        └── style.css
```

### What you will practice

- OOP: encapsulation, constructors with validation, inheritance (`extends`, `super`), polymorphism (`@Override toString()`), constants (`static final`), exceptions.
- Collections: `List` / `ArrayList`, iterating and computing an average and a maximum.
- JavaFX: `Application`, `Stage`, `Scene`, layouts (`VBox`, `HBox`), controls (`Label`, `TextField`, `Button`, `ListView`).
- FXML: `fx:controller`, `fx:id`, `onAction="#…"`, `@FXML`, `initialize()`.
- JavaFX CSS: `-fx-` properties, style classes, pseudo-classes (`:hover`, `:focused`, `:selected`, `:odd`), looked-up colors (variables), `derive()`.
- Java modules: `module-info.java`, `requires`, `exports`, `opens`.

> **Setup:** before starting, read the [README](../README.md) to install the JDK and set up Eclipse or VS Code.

---

## Part 0: Project setup

### Question 0.1: Gradle configuration

Create an empty folder for the project and add two files:

**`settings.gradle`**: set the project name to `student-grade-manager-fxml`.

**`build.gradle`**:

- apply the `application` plugin and the `org.openjfx.javafxplugin` plugin, version `0.1.0`;
- use `mavenCentral()` as the repository;
- make every `JavaCompile` task use `release = 17` and `encoding = 'UTF-8'`;
- in the `javafx { }` block, use JavaFX version `21.0.5` with the modules `javafx.controls` and `javafx.fxml`;
- in the `application { }` block, set `mainModule = 'studentapp'` and `mainClass = 'studentapp.StudentApp'`.

Create the folders `src/main/java/studentapp/` and `src/main/resources/studentapp/`.

> 💡 Why do we need a plugin for JavaFX? Since Java 11, JavaFX is no longer part of the JDK. The plugin downloads the right JavaFX jars for your operating system.

### Question 0.2: The module descriptor

Create `src/main/java/module-info.java` that declares a module named **`studentapp`** which:

- **requires** `javafx.controls` and `javafx.fxml`;
- **exports** the package `studentapp`, because JavaFX must be able to create our `Application` subclass;
- **opens** the package `studentapp` **to** `javafx.fxml`.

**Answer in a comment:** what is the difference between `exports` and `opens`? What error would you get if `opens` were missing, and why?

---

## Part 1: The model (plain Java, no JavaFX)

### Question 1.1: The `Person` class

Create `src/main/java/studentapp/Person.java` in the package `studentapp`.

| Member | Description |
|--------|-------------|
| `private final String name` | The person's name. It cannot change after creation. |
| `public Person(String name)` | Constructor. If `name` is `null` or blank (empty or only spaces), throw an `IllegalArgumentException` with the message `"The name cannot be empty."`. Otherwise store the name **trimmed** (without the spaces at the start and the end). |
| `public String getName()` | Returns the name. |
| `public String toString()` | `@Override`. Returns the name. |

**Test it** (in a temporary `main` or in JShell):

```java
new Person("  Sara ").getName();   // "Sara"
new Person("   ");                 // IllegalArgumentException: The name cannot be empty.
```

### Question 1.2: The `Student` class

Create `Student.java`. A `Student` **is a** `Person` that has a grade.

| Member | Description |
|--------|-------------|
| `public static final double MIN_GRADE = 0` | The lowest allowed grade. |
| `public static final double MAX_GRADE = 20` | The highest allowed grade. |
| `public static final double PASSING_GRADE = 10` | The grade needed to pass. |
| `private final double grade` | The student's grade. |
| `public Student(String name, double grade)` | Calls the parent constructor with `name`, so the name is checked by `Person`. If `grade` is below `MIN_GRADE` or above `MAX_GRADE`, throws an `IllegalArgumentException` with the message `"The grade must be between 0.0 and 20.0."`. Build the message from the constants. |
| `public double getGrade()` | Returns the grade. |
| `public boolean isPassing()` | Returns `true` if `grade >= PASSING_GRADE`. |
| `public String toString()` | `@Override`. Returns for example `"Sara  -  15.5 / 20  (Passed)"` or `"Adam  -  8.0 / 20  (Failed)"`. There are **two spaces** around the dash and before the parenthesis. |

**Questions:**
1. Why must `super(name)` be the **first** statement of the constructor?
2. Later, the `ListView` will show `Student` objects. Which method will it call to know what text to display? Which OOP concept is this?

### Question 1.3: The `StudentManager` class

Create `StudentManager.java`. It stores the students and computes the statistics. It knows **nothing** about the GUI.

| Member | Description |
|--------|-------------|
| `private final List<Student> students` | Initialized with a new `ArrayList<>()`. |
| `public void addStudent(Student student)` | Adds the student to the list. |
| `public void removeStudent(Student student)` | Removes the student from the list. |
| `public int getCount()` | Returns the number of students. |
| `public double getAverage()` | Returns the average of all grades, or `0` if the list is empty (so we never divide by zero). |
| `public Student getBestStudent()` | Returns the student with the **highest** grade, or `null` if the list is empty. If two students have the same best grade, return the **first** one added. |

**Test it:**

```java
StudentManager m = new StudentManager();
m.getAverage();                       // 0.0
m.getBestStudent();                   // null
m.addStudent(new Student("Sara", 15.5));
m.addStudent(new Student("Adam", 8));
m.getCount();                         // 2
m.getAverage();                       // 11.75
m.getBestStudent().getName();         // "Sara"
```

**Question:** give two advantages of keeping `StudentManager` separate from the GUI.

---

## Part 2: The entry point `StudentApp`

`StudentApp` is short in this version, but it has several important ideas, so we build it in three steps.

### Question 2.1: The skeleton

Create `StudentApp.java`:

- the class **extends** `javafx.application.Application`;
- it has a `public static void main(String[] args)` method that only calls `launch(args)`;
- it **overrides** `public void start(Stage stage) throws IOException`. Leave it empty for now.

**Question:** we never write `new StudentApp()` and never call `start(...)` ourselves. Who does it? Explain in your own words what `launch(args)` does.

### Question 2.2: Loading the FXML file

In `start(...)`:

1. Create an `FXMLLoader` for the resource `"student-view.fxml"`. Use `StudentApp.class.getResource(...)` so the file is found **in the same package** as `StudentApp`, that is in `src/main/resources/studentapp/`.
2. Call `loader.load()` and store the result in a variable of type `javafx.scene.Parent` called `root`.

**Question:** why is `Parent` a good type for `root`, even though the root of our FXML will be a `VBox`?

> ⚠️ The FXML file doesn't exist yet. This code compiles, but running it will fail until you finish Part 3.

### Question 2.3: Scene and Stage

Still in `start(...)`:

1. Create a `Scene` that contains `root`, with a size of **560 × 520**.
2. Set the window title to `"JavaFX Demo - Student Grade Manager (FXML + CSS)"`.
3. Put the scene on the stage and show the stage.

**Question:** explain the "theater" metaphor: what do `Stage`, `Scene` and the nodes stand for?

**Bonus:** the CSS file will be attached inside the FXML. Write (as a comment) the single line of Java that would attach `style.css` to the `Scene` instead.

---

## Part 3: The view `student-view.fxml`

Create `src/main/resources/studentapp/student-view.fxml`. We build the scene graph step by step. This is the tree we want:

```
VBox (root)
  ├─ Label      "Student Grade Manager"
  ├─ Label      "Built with FXML + CSS"
  ├─ HBox       (the form)
  │    ├─ TextField  (name)
  │    ├─ TextField  (grade)
  │    └─ Button     "Add"
  ├─ ListView   (the students)
  ├─ Button     "Remove selected"
  ├─ Label      (statistics)
  └─ Label      (messages)
```

### Question 3.1: Header, imports and the root node

1. Start with the XML declaration `<?xml version="1.0" encoding="UTF-8"?>`.
2. Add the `<?import …?>` instructions for every class you will use: `Insets`, `Button`, `Label`, `ListView`, `TextField`, `HBox`, `VBox`.
3. Create the root `<VBox>` with:
   - the namespaces `xmlns="http://javafx.com/javafx/21"` and `xmlns:fx="http://javafx.com/fxml/1"`;
   - `fx:controller="studentapp.StudentController"`;
   - `stylesheets="@style.css"`;
   - `styleClass="root-pane"`;
   - `spacing="12"`.
4. Inside the `VBox`, set a **padding of 20** on all four sides with a `<padding><Insets …/></padding>` element.

**Question:** what does the `@` in `@style.css` mean?

### Question 3.2: The titles

Add two `Label`s:

- the text `Student Grade Manager` with the style class `title`;
- the text `Built with FXML + CSS` with the style class `subtitle`.

### Question 3.3: The input form

Add an `HBox` with `spacing="10"`, `alignment="CENTER_LEFT"` and `styleClass="form"`, containing:

| Node | Attributes |
|------|-----------|
| `TextField` | `fx:id="nameField"`, `promptText="Student name"`, and it must **grow horizontally** to take the free space (`HBox.hgrow="ALWAYS"`). |
| `TextField` | `fx:id="gradeField"`, `promptText="Grade (0 - 20)"`, `prefWidth="120"`, and `onAction="#handleAdd"` so that pressing **ENTER** adds the student. |
| `Button` | `text="Add"`, `onAction="#handleAdd"`, `styleClass="primary-button"`. |

**Questions:**
1. What is the link between `fx:id="nameField"` and the Java controller?
2. `HBox.hgrow` is called a *static property* (or *layout constraint*). Why is it written on the `TextField` and not on the `HBox`?

### Question 3.4: The student list

Add a `ListView` with `fx:id="studentList"` that **grows vertically** to take all the remaining space (`VBox.vgrow="ALWAYS"`).

When the list is empty it must show a **placeholder**: a `Label` with the text `No students yet. Add one above!` and the style class `placeholder`. Use the `<placeholder>` property element.

### Question 3.5: Remove button, statistics and messages

Add at the end of the `VBox`:

- a `Button` with the text `Remove selected`, `onAction="#handleRemove"` and `styleClass="danger-button"`;
- a `Label` with `fx:id="statsLabel"` and `styleClass="stats"`;
- a `Label` with `fx:id="messageLabel"` and `styleClass="message"`.

**Question:** XML comments `<!-- … -->` are welcome in this file, but one sequence of characters is **forbidden** inside them. Which one? What happens if you use it?

> 💡 **Bonus:** open your FXML file in [Scene Builder](https://gluonhq.com/products/scene-builder/) and check that the tree matches the diagram above.

---

## Part 4: The controller `StudentController`

Create `StudentController.java`. It contains **only behavior**: no `new Button(...)`, no `setStyle(...)`, no `setOnAction(...)`.

### Question 4.1: Fields

Declare:

| Field | Description |
|-------|-------------|
| `private final StudentManager manager` | Initialized with `new StudentManager()`. This is the link to the model. |
| `@FXML private TextField nameField` | Injected by FXMLLoader. |
| `@FXML private TextField gradeField` | Injected by FXMLLoader. |
| `@FXML private ListView<Student> studentList` | Injected by FXMLLoader. Note the generic type. |
| `@FXML private Label statsLabel` | Injected by FXMLLoader. |
| `@FXML private Label messageLabel` | Injected by FXMLLoader. |

**Questions:**
1. These fields are `private`. How can FXMLLoader still assign them? Which line of `module-info.java` makes it possible?
2. What happens if a field is named `namefield` while the FXML says `fx:id="nameField"`?

### Question 4.2: `updateStatistics()` (private helper)

Write `private void updateStatistics()` that updates `statsLabel`:

- if there are no students: `Students: 0   |   Average: -   |   Best: -`
- otherwise, use `String.format` to get for example: `Students: 2   |   Average: 11.75 / 20   |   Best: Sara` (the average has **2 decimals**).

There are **three spaces** around each `|`.

### Question 4.3: `showMessage(String text, boolean isError)` (private helper)

Write a method that:

1. sets the text of `messageLabel`;
2. removes the style classes `"error"` and `"success"` from `messageLabel`;
3. adds `"error"` if `isError` is `true`, otherwise `"success"`.

**Question:** why do we switch a **style class** instead of calling `messageLabel.setStyle("-fx-text-fill: red;")`?

### Question 4.4: `initialize()`

Write a method `@FXML private void initialize()` that calls `updateStatistics()`, so the statistics line is filled when the window opens.

**Question:** why can't we do this in the controller's **constructor**?

### Question 4.5: `handleAdd()`

Write `@FXML private void handleAdd()`. It is called by the **Add** button and by **ENTER** in the grade field.

1. Read the name from `nameField` and the grade text from `gradeField` (trimmed).
2. Inside a `try` block:
   - convert the grade text with `Double.parseDouble(...)`;
   - create a `new Student(name, grade)`;
   - add it to the `manager` **and** to `studentList.getItems()`;
   - clear both fields and put the focus back on `nameField`;
   - show the success message `"Added <name>."`;
   - update the statistics.
3. Catch the exceptions **in this order**:
   - `NumberFormatException`: show the error `"The grade must be a number (example: 14.5)."`;
   - `IllegalArgumentException`: show the exception's own message (it comes from `Person` or `Student`) as an error.

**Questions:**
1. `NumberFormatException` is a subclass of `IllegalArgumentException`. What would happen if the two `catch` blocks were in the opposite order?
2. We never call something like `studentList.refresh()`. Why does the list update by itself?

### Question 4.6: `handleRemove()`

Write `@FXML private void handleRemove()`:

1. Get the selected student with `studentList.getSelectionModel().getSelectedItem()`.
2. If nothing is selected, show the error `"Select a student in the list first."` and stop.
3. Otherwise remove it from the `manager` **and** from the list, show `"Removed <name>."` and update the statistics.

### ✅ Checkpoint

Run the app (`gradlew run`). It must work completely, even though it still looks plain because the CSS file is empty or missing. Test these cases:

| Input | Expected message |
|-------|------------------|
| name `Sara`, grade `15.5` | Added Sara. (the list and statistics update) |
| name `Adam`, grade `8` + ENTER | Added Adam. (Average: 11.75, Best: Sara) |
| name empty, grade `12` | The name cannot be empty. |
| name `Ali`, grade `abc` | The grade must be a number (example: 14.5). |
| name `Ali`, grade `25` | The grade must be between 0.0 and 20.0. |
| click *Remove selected* with nothing selected | Select a student in the list first. |
| select Sara, click *Remove selected* | Removed Sara. (Best: Adam) |

---

## Part 5: The style `style.css`

Create `src/main/resources/studentapp/style.css`. Remember: JavaFX CSS properties start with **`-fx-`**.

### Question 5.1: Color variables and global font

In the `.root` selector, define these **looked-up colors** (JavaFX's version of CSS variables):

| Variable | Value |
|----------|-------|
| `-primary` | `#2e6cdf` |
| `-primary-dark` | `#1f4fa8` |
| `-danger` | `#c0392b` |
| `-success` | `#27ae60` |
| `-text` | `#2c3e50` |
| `-muted` | `#7f8c8d` |

Also set the font family (`"Segoe UI", "Arial", sans-serif`) and a font size of `14px` for the whole app.

### Question 5.2: Background and titles

- `.root-pane`: a vertical **linear gradient** background from `#f4f7fb` (top) to `#e6ecf5` (bottom).
- `.title`: size `24px`, bold, color `-text`.
- `.subtitle`: color `-muted`, italic, and a padding of `-8 0 4 0` to pull it closer to the title.

### Question 5.3: Text fields

- `.text-field`: white background, background radius `6`, border color `#cfd8e3`, border radius `6`, padding `7 10`.
- When a text field **has the focus**, its border color becomes `-primary`. Use the correct pseudo-class.

### Question 5.4: Buttons

- `.button` (all buttons): background radius `6`, padding `7 16`, hand cursor, bold white text.
- `.primary-button`: background `-primary`. On **hover**: `-primary-dark`.
- `.danger-button`: background `-danger`. On **hover**: a color **20% darker** than `-danger`, computed with the `derive()` function.
- When **any** button is **pressed**, scale it to `0.97` on both axes.

### Question 5.5: The list

- `.list-view`: background radius `8`, border color `#cfd8e3`, border radius `8`.
- `.list-cell`: padding `8 10`, text color `-text`.
- **Odd** rows: background `#f8fafd` (zebra stripes).
- A **filled** cell that is **hovered**: background `#e3ecfb`.
- A **filled** cell that is **selected**: background `-primary`, white text.
- `.placeholder`: text color `-muted`.

**Question:** why do we write `.list-cell:filled:hover` and not only `.list-cell:hover`?

### Question 5.6: Statistics and messages

- `.stats`: bold, color `-text`.
- `.message.error`: text color `-danger`.
- `.message.success`: text color `-success`.

**Question:** what does the selector `.message.error` (no space) match? How is it different from `.message .error` (with a space)?

### ✅ Final checkpoint

Run the app again. It should now look like the screenshot from the problem statement: a blue **Add** button, a red **Remove** button, a striped list and colored messages.

---

## Part 6: Going further (bonus)

1. **Theming:** change only `-primary` in `style.css` to `#8e44ad` and re-run. Which parts of the UI changed? How many lines of Java did you touch?
2. **Break it on purpose:** rename `fx:id="statsLabel"` to `fx:id="statLabel"` in the FXML. Run the app and explain the error. Do the same with `opens` in `module-info.java`.
3. **Dark mode:** create `dark.css` that overrides the variables, and add a `CheckBox` "Dark mode" that swaps the stylesheet of the scene at runtime.
4. **Duplicate names:** forbid adding two students with the same name (ignoring case). Where should this rule go: in the controller or in `StudentManager`? Justify.
5. **Counter of passing students:** add `getPassingCount()` to `StudentManager` and show `Passed: x / n` in the statistics line.
6. **Unit tests:** add JUnit 5 to `build.gradle` and write tests for `Student` (validation, `isPassing`, `toString`) and `StudentManager` (`getAverage`, `getBestStudent` with an empty list, ties).
7. **Edit a grade:** double-click a student in the list to change their grade. (Hint: `Student` is immutable, so think about replacing the object.)
