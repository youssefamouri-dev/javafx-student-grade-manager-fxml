package studentapp;

import java.util.ArrayList;
import java.util.List;

/*
 * ============================================================================
 *  StudentManager  -  the "logic" of the application (still NO JavaFX!)
 * ============================================================================
 *  This class stores the students and does the calculations.
 *  It knows NOTHING about windows or buttons.
 *
 *  Why separate it from the GUI?
 *   - We could reuse it in a console program, a Swing program or a website.
 *   - It is easier to test and to understand.
 *  The GUI (StudentApp) will just CALL these methods.
 */
public class StudentManager {

    // A classic Java list of Student objects.
    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public int getCount() {
        return students.size();
    }

    // Average of all grades (0 if the list is empty, to avoid dividing by 0).
    public double getAverage() {
        if (students.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (Student s : students) {
            sum += s.getGrade();
        }
        return sum / students.size();
    }

    // Returns the student with the highest grade, or null if the list is empty.
    public Student getBestStudent() {
        Student best = null;
        for (Student s : students) {
            if (best == null || s.getGrade() > best.getGrade()) {
                best = s;
            }
        }
        return best;
    }
}
