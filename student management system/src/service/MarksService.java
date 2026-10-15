package service;

import dao.MarksDAO;
import dao.StudentDAOImpl;
import model.MarkRecord;

import java.util.List;

public class MarksService {

    private final MarksDAO dao;
    private final StudentDAOImpl studentDAO;

    public MarksService(StudentDAOImpl studentDAO) {
        this.dao = new MarksDAO();
        this.studentDAO = studentDAO;
    }

    public String addMarks(int studentId, String subject, double marksObtained, double maxMarks) {
        if (!studentDAO.existsById(studentId)) {
            return "Error: No student found with ID " + studentId + ".";
        }
        if (subject == null || subject.isBlank()) {
            return "Error: Subject cannot be empty.";
        }
        if (maxMarks <= 0) {
            return "Error: Max marks must be greater than 0.";
        }
        if (marksObtained < 0 || marksObtained > maxMarks) {
            return "Error: Marks obtained must be between 0 and " + maxMarks + ".";
        }
        dao.add(studentId, subject.trim(), marksObtained, maxMarks);
        return "OK";
    }

    public List<MarkRecord> getRecords(int studentId) {
        return dao.getByStudent(studentId);
    }

    /** All marks records across every student -- used by the dashboard's subject-wise chart. */
    public List<MarkRecord> getAllRecords() {
        return dao.getAll();
    }

    /** Weighted average percentage across all of a student's subjects. */
    public double getAveragePercentage(int studentId) {
        List<MarkRecord> records = dao.getByStudent(studentId);
        if (records.isEmpty()) {
            return 0.0;
        }
        double totalObtained = records.stream().mapToDouble(MarkRecord::getMarksObtained).sum();
        double totalMax = records.stream().mapToDouble(MarkRecord::getMaxMarks).sum();
        if (totalMax == 0) {
            return 0.0;
        }
        return Math.round((totalObtained / totalMax) * 10000.0) / 100.0;
    }

    public String getGrade(double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B+";
        if (percentage >= 60) return "B";
        if (percentage >= 50) return "C";
        if (percentage >= 40) return "D";
        return "F";
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }
}
