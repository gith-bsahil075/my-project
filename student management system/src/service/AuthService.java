package service;

import dao.AdminDAO;
import dao.StudentCredentialDAO;
import dao.StudentDAOImpl;
import util.SessionManager;

public class AuthService {

    private final AdminDAO adminDAO;
    private final StudentCredentialDAO credentialDAO;
    private final StudentDAOImpl studentDAO;

    public AuthService(StudentDAOImpl studentDAO) {
        this.adminDAO = new AdminDAO();
        this.credentialDAO = new StudentCredentialDAO();
        this.studentDAO = studentDAO;
    }

    /** Returns a new ADMIN session token if the credentials are correct, otherwise null. */
    public String login(String username, String password) {
        if (adminDAO.verify(username, password)) {
            return SessionManager.createAdminSession();
        }
        return null;
    }

    /** Returns a new STUDENT session token if the studentId/password are correct, otherwise null. */
    public String studentLogin(int studentId, String password) {
        if (credentialDAO.verify(studentId, password)) {
            return SessionManager.createStudentSession(studentId);
        }
        return null;
    }

    public void logout(String token) {
        SessionManager.invalidate(token);
    }

    /** Admin action: (re)generates a student's portal password. Returns the new PLAINTEXT password. */
    public String generateStudentLogin(int studentId) {
        if (!studentDAO.existsById(studentId)) {
            return null;
        }
        return credentialDAO.generatePassword(studentId);
    }

    public boolean studentHasLogin(int studentId) {
        return credentialDAO.hasLogin(studentId);
    }

    public void deleteStudentLogin(int studentId) {
        credentialDAO.deleteLogin(studentId);
    }
}
