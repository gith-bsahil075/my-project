package dao;

import model.Student;
import java.util.List;

/**
 * Data Access contract for Student records.
 * Kept as an interface so the storage mechanism (file, DB, etc.)
 * can be swapped without touching the Service layer.
 */
public interface StudentDAO {

    void addStudent(Student student);

    List<Student> getAllStudents();

    Student getStudentById(int id);

    boolean updateStudent(Student student);

    boolean deleteStudent(int id);

    boolean existsById(int id);
}
