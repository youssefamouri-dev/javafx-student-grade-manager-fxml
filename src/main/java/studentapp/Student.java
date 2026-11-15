package studentapp;

/*
 * ============================================================================
 *  Student  -  a NORMAL Java class that INHERITS from Person
 * ============================================================================
 *  Still no JavaFX here. This is our "model": the data of the application.
 *
 *  OOP concepts used:
 *   - Inheritance  : Student extends Person (a Student IS A Person).
 *   - super(...)   : calls the constructor of the parent class.
 *   - Polymorphism : we OVERRIDE toString(). Later, the JavaFX ListView will
 *                    call toString() on each object to know what text to show.
 */
public class Student extends Person {

    public static final double MIN_GRADE = 0;
    public static final double MAX_GRADE = 20;
    public static final double PASSING_GRADE = 10;

    private final double grade;

    public Student(String name, double grade) {
        super(name); // Person's constructor checks the name

        // Business rule: the grade must be between 0 and 20.
        if (grade < MIN_GRADE || grade > MAX_GRADE) {
            throw new IllegalArgumentException(
                    "The grade must be between " + MIN_GRADE + " and " + MAX_GRADE + ".");
        }
        this.grade = grade;
    }

    public double getGrade() {
        return grade;
    }

    public boolean isPassing() {
        return grade >= PASSING_GRADE;
    }

    // Polymorphism: this version of toString() replaces the one in Person.
    // Example result: "Sara  -  15.5 / 20  (Passed)"
    @Override
    public String toString() {
        String status = isPassing() ? "Passed" : "Failed";
        return getName() + "  -  " + grade + " / 20  (" + status + ")";
    }
}
