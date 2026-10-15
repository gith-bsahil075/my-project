package dao;

import model.MarkRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MarksDAO {

    private final List<MarkRecord> records;
    private static final String FILE_PATH = "data" + File.separator + "marks.txt";
    private int nextId;

    public MarksDAO() {
        records = new ArrayList<>();
        loadFromFile();
        nextId = records.stream().mapToInt(MarkRecord::getId).max().orElse(0) + 1;
    }

    public MarkRecord add(int studentId, String subject, double marksObtained, double maxMarks) {
        MarkRecord record = new MarkRecord(nextId++, studentId, subject, marksObtained, maxMarks);
        records.add(record);
        saveToFile();
        return record;
    }

    public List<MarkRecord> getByStudent(int studentId) {
        return records.stream()
                .filter(r -> r.getStudentId() == studentId)
                .collect(Collectors.toList());
    }

    /** All records across every student -- used by the dashboard's subject-wise marks chart. */
    public List<MarkRecord> getAll() {
        return new ArrayList<>(records);
    }

    public boolean delete(int id) {
        boolean removed = records.removeIf(r -> r.getId() == id);
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    private void saveToFile() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (MarkRecord r : records) {
                writer.println(r.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Could not save marks data: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    records.add(MarkRecord.fromFileString(line));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load marks data: " + e.getMessage());
        }
    }
}
