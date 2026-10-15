package service;

import dao.FeeDAO;
import dao.StudentDAOImpl;
import model.Fee;

import java.util.Map;

public class FeeService {

    private final FeeDAO dao;
    private final StudentDAOImpl studentDAO;

    public FeeService(StudentDAOImpl studentDAO) {
        this.dao = new FeeDAO();
        this.studentDAO = studentDAO;
    }

    public String setFee(int studentId, double totalFee, double paidAmount) {
        if (!studentDAO.existsById(studentId)) {
            return "Error: No student found with ID " + studentId + ".";
        }
        if (totalFee < 0 || paidAmount < 0) {
            return "Error: Amounts cannot be negative.";
        }
        dao.upsert(studentId, totalFee, paidAmount);
        return "OK";
    }

    /** Fee record for a student, or a zeroed default if none has been set yet. */
    public Fee getFee(int studentId) {
        Fee fee = dao.get(studentId);
        return fee != null ? fee : new Fee(studentId, 0, 0);
    }

    public Map<Integer, Fee> getAllFees() {
        return dao.getAll();
    }
}
