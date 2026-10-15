package model;

/**
 * Represents a single Student record.
 * Plain model class (POJO) used across the DAO and Service layers.
 */
public class Student {

    private int id;
    private String name;
    private int age;
    private String course;
    private double cgpa;
    private long createdAt; // epoch millis; used for the dashboard "growth over time" chart

    public Student(int id, String name, int age, String course, double cgpa) {
        this(id, name, age, course, cgpa, System.currentTimeMillis());
    }

    public Student(int id, String name, int age, String course, double cgpa, long createdAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
        this.cgpa = cgpa;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    /** Row format used for the console table view. */
    @Override
    public String toString() {
        return String.format("%-6d %-20s %-5d %-15s %-5.2f", id, name, age, course, cgpa);
    }

    /** Serializes this student to a JSON object string, for the REST API layer. */
    public String toJson() {
        return String.format(
                "{\"id\":%d,\"name\":%s,\"age\":%d,\"course\":%s,\"cgpa\":%.2f,\"createdAt\":%d}",
                id, JsonSupport.quote(name), age, JsonSupport.quote(course), cgpa, createdAt
        );
    }

    /** Serializes this student into one CSV line for file storage. */
    public String toFileString() {
        return id + "," + name + "," + age + "," + course + "," + cgpa + "," + createdAt;
    }

    /**
     * Parses one CSV line back into a Student. Tolerates OLDER 5-field rows
     * (written before createdAt existed) by defaulting createdAt to 0 --
     * so upgrading to this version never breaks existing data files.
     */
    public static Student fromFileString(String line) {
        String[] parts = line.split(",", -1);
        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        int age = Integer.parseInt(parts[2].trim());
        String course = parts[3].trim();
        double cgpa = Double.parseDouble(parts[4].trim());
        long createdAt = parts.length >= 6 ? Long.parseLong(parts[5].trim()) : 0L;
        return new Student(id, name, age, course, cgpa, createdAt);
    }
}
