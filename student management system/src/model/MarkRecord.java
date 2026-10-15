package model;

/** One subject's marks for one student. */
public class MarkRecord {

    private int id;
    private int studentId;
    private String subject;
    private double marksObtained;
    private double maxMarks;

    public MarkRecord(int id, int studentId, String subject, double marksObtained, double maxMarks) {
        this.id = id;
        this.studentId = studentId;
        this.subject = subject;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
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

    public double getMarksObtained() {
        return marksObtained;
    }

    public double getMaxMarks() {
        return maxMarks;
    }

    public String toJson() {
        return String.format(
                "{\"id\":%d,\"studentId\":%d,\"subject\":%s,\"marksObtained\":%.2f,\"maxMarks\":%.2f}",
                id, studentId, JsonSupport.quote(subject), marksObtained, maxMarks
        );
    }

    public String toFileString() {
        return id + "," + studentId + "," + subject + "," + marksObtained + "," + maxMarks;
    }

    public static MarkRecord fromFileString(String line) {
        String[] p = line.split(",", -1);
        return new MarkRecord(
                Integer.parseInt(p[0].trim()), Integer.parseInt(p[1].trim()),
                p[2].trim(), Double.parseDouble(p[3].trim()), Double.parseDouble(p[4].trim())
        );
    }
}
