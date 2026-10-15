package service;

import dao.StudentDAO;
import dao.StudentDAOImpl;
import model.Student;

import java.util.Comparator;
import java.util.List;

/**
 * Business logic + validation layer, sitting between the console UI (Main)
 * and the DAO. Main never talks to StudentDAO directly.
 */
public class StudentService {

    private final StudentDAO dao;

    public StudentService() {
        this.dao = new StudentDAOImpl();
    }

    /** Allows sharing one StudentDAOImpl instance across multiple services (used by ServerMain). */
    public StudentService(StudentDAO dao) {
        this.dao = dao;
    }

    public String addStudent(int id, String name, int age, String course, double cgpa) {
        String error = validate(id, name, age, cgpa, true);
        if (error != null) {
            return error;
        }
        dao.addStudent(new Student(id, name, age, course, cgpa));
        return "Student added successfully.";
    }

    public List<Student> getAllStudents() {
        List<Student> all = dao.getAllStudents();
        all.sort(Comparator.comparingInt(Student::getId));
        return all;
    }

    public Student searchStudent(int id) {
        return dao.getStudentById(id);
    }

    public String updateStudent(int id, String name, int age, String course, double cgpa) {
        Student existing = dao.getStudentById(id);
        if (existing == null) {
            return "Error: No student found with ID " + id + ".";
        }
        String error = validate(id, name, age, cgpa, false);
        if (error != null) {
            return error;
        }
        dao.updateStudent(new Student(id, name, age, course, cgpa, existing.getCreatedAt()));
        return "Student updated successfully.";
    }

    public String deleteStudent(int id) {
        if (dao.deleteStudent(id)) {
            return "Student deleted successfully.";
        }
        return "Error: No student found with ID " + id + ".";
    }

    public int totalStudents() {
        return dao.getAllStudents().size();
    }

    /**
     * Next free ID (max existing id + 1). Used by the web layer so users
     * filling a browser form never have to pick an ID by hand.
     */
    public int nextAvailableId() {
        return dao.getAllStudents().stream()
                .mapToInt(Student::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Shared validation for add/update.
     * checkDuplicateId is true only for "add", since update legitimately
     * reuses the existing id.
     */
    private String validate(int id, String name, int age, double cgpa, boolean checkDuplicateId) {
        if (id <= 0) {
            return "Error: ID must be a positive number.";
        }
        if (checkDuplicateId && dao.existsById(id)) {
            return "Error: Student with ID " + id + " already exists.";
        }
        if (name == null || name.isBlank()) {
            return "Error: Name cannot be empty.";
        }
        if (age <= 0 || age > 100) {
            return "Error: Age must be between 1 and 100.";
        }
        if (cgpa < 0 || cgpa > 10) {
            return "Error: CGPA must be between 0.0 and 10.0.";
        }
        return null;
    }
}
