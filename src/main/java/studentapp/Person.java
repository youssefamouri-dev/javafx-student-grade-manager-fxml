package studentapp;

/*
 * ============================================================================
 *  Person  -  a NORMAL Java class (no JavaFX here!)
 * ============================================================================
 *  This is the kind of class you already know from the OOP course:
 *  private attribute, constructor, getter, toString().
 *
 *  It is the PARENT class of Student (inheritance).
 */
public class Person {

    // Encapsulation: the attribute is private, accessible only through methods.
    private final String name;

    // Constructor with a small validation rule.
    public Person(String name) {
        if (name == null || name.isBlank()) {
            // We throw an exception: the GUI will catch it and show a message.
            throw new IllegalArgumentException("The name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
