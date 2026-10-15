package service;

import dao.AttendanceDAO;
import dao.StudentDAOImpl;
import model.AttendanceRecord;

import java.util.List;

public class AttendanceService {

    private final AttendanceDAO dao;
    private final StudentDAOImpl studentDAO;

    /** @param studentDAO the SAME StudentDAOImpl instance used elsewhere in the app,
     *                    so student existence checks always see current data. */
    public AttendanceService(StudentDAOImpl studentDAO) {
        this.dao = new AttendanceDAO();
        this.studentDAO = studentDAO;
    }

    public String markAttendance(int studentId, String subject, String date, String status) {
        if (!studentDAO.existsById(studentId)) {
            return "Error: No student found with ID " + studentId + ".";
        }
        if (subject == null || subject.isBlank()) {
            return "Error: Subject cannot be empty.";
        }
        if (date == null || date.isBlank()) {
            return "Error: Date cannot be empty.";
        }
        if (!status.equalsIgnoreCase("Present") && !status.equalsIgnoreCase("Absent")) {
            return "Error: Status must be Present or Absent.";
        }
        dao.add(studentId, subject.trim(), date.trim(), status);
        return "OK";
    }

    public List<AttendanceRecord> getRecords(int studentId) {
        return dao.getByStudent(studentId);
    }

    /** All attendance records across every student -- used by the dashboard. */
    public List<AttendanceRecord> getAllRecords() {
        return dao.getAll();
    }

    public double getPercentage(int studentId) {
        List<AttendanceRecord> records = dao.getByStudent(studentId);
        if (records.isEmpty()) {
            return 0.0;
        }
        long present = records.stream().filter(AttendanceRecord::isPresent).count();
        return Math.round((present * 10000.0) / records.size()) / 100.0;
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }
}
