package dao;

import model.Student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * File-backed implementation of StudentDAO.
 * Keeps an in-memory list for fast access and mirrors every change
 * to a plain text file (data/students.txt) so records survive restarts.
 * No external database/driver needed -> runs as-is in VS Code.
 */
public class StudentDAOImpl implements StudentDAO {

    private final List<Student> students;
    private static final String FILE_PATH = "data" + File.separator + "students.txt";

    public StudentDAOImpl() {
        students = new ArrayList<>();
        loadFromFile();
    }

    @Override
    public void addStudent(Student student) {
        students.add(student);
        saveToFile();
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    @Override
    public Student getStudentById(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    @Override
    public boolean updateStudent(Student updated) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == updated.getId()) {
                students.set(i, updated);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteStudent(int id) {
        boolean removed = students.removeIf(s -> s.getId() == id);
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    @Override
    public boolean existsById(int id) {
        return getStudentById(id) != null;
    }

    /** Writes the full in-memory list to disk, overwriting the old file. */
    private void saveToFile() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Student s : students) {
                writer.println(s.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    /** Loads existing records on startup, if the data file already exists. */
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    students.add(Student.fromFileString(line));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load existing data: " + e.getMessage());
        }
    }
}
