package dao;

import model.AttendanceRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceDAO {

    private final List<AttendanceRecord> records;
    private static final String FILE_PATH = "data" + File.separator + "attendance.txt";
    private int nextId;

    public AttendanceDAO() {
        records = new ArrayList<>();
        loadFromFile();
        nextId = records.stream().mapToInt(AttendanceRecord::getId).max().orElse(0) + 1;
    }

    public AttendanceRecord add(int studentId, String subject, String date, String status) {
        AttendanceRecord record = new AttendanceRecord(nextId++, studentId, subject, date, status);
        records.add(record);
        saveToFile();
        return record;
    }

    public List<AttendanceRecord> getByStudent(int studentId) {
        return records.stream()
                .filter(r -> r.getStudentId() == studentId)
                .collect(Collectors.toList());
    }

    /** All records across every student -- used by the dashboard's attendance overview chart. */
    public List<AttendanceRecord> getAll() {
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
            for (AttendanceRecord r : records) {
                writer.println(r.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Could not save attendance data: " + e.getMessage());
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
                    records.add(AttendanceRecord.fromFileString(line));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load attendance data: " + e.getMessage());
        }
    }
}
