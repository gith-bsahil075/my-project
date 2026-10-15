package model;

/** One attendance entry: a student's status for one subject on one date. */
public class AttendanceRecord {

    private int id;
    private int studentId;
    private String subject;
    private String date;   // stored as plain "yyyy-MM-dd" text, no external date lib needed
    private String status; // "Present" or "Absent"

    public AttendanceRecord(int id, int studentId, String subject, String date, String status) {
        this.id = id;
        this.studentId = studentId;
        this.subject = subject;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getSubject() {
        return subject;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public boolean isPresent() {
        return "Present".equalsIgnoreCase(status);
    }

    public String toJson() {
        return String.format(
                "{\"id\":%d,\"studentId\":%d,\"subject\":%s,\"date\":%s,\"status\":%s}",
                id, studentId, JsonSupport.quote(subject), JsonSupport.quote(date), JsonSupport.quote(status)
        );
    }

    public String toFileString() {
        return id + "," + studentId + "," + subject + "," + date + "," + status;
    }

    public static AttendanceRecord fromFileString(String line) {
        String[] p = line.split(",", -1);
        return new AttendanceRecord(
                Integer.parseInt(p[0].trim()), Integer.parseInt(p[1].trim()),
                p[2].trim(), p[3].trim(), p[4].trim()
        );
    }
}
