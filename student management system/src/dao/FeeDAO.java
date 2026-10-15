package dao;

import model.Fee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class FeeDAO {

    private final Map<Integer, Fee> feesByStudentId;
    private static final String FILE_PATH = "data" + File.separator + "fees.txt";

    public FeeDAO() {
        feesByStudentId = new LinkedHashMap<>();
        loadFromFile();
    }

    public Fee get(int studentId) {
        return feesByStudentId.get(studentId);
    }

    public Map<Integer, Fee> getAll() {
        return new LinkedHashMap<>(feesByStudentId);
    }

    public void upsert(int studentId, double totalFee, double paidAmount) {
        feesByStudentId.put(studentId, new Fee(studentId, totalFee, paidAmount));
        saveToFile();
    }

    private void saveToFile() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Fee f : feesByStudentId.values()) {
                writer.println(f.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Could not save fee data: " + e.getMessage());
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
                    Fee fee = Fee.fromFileString(line);
                    feesByStudentId.put(fee.getStudentId(), fee);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load fee data: " + e.getMessage());
        }
    }
}
