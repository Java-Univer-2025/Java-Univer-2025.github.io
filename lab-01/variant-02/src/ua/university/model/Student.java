package ua.university.model;

import java.util.Objects;
import ua.university.util.PersonUtils;
import ua.university.util.StudentUtils;

public class Student extends Person {

    private String studentId;
    private Group group;

    private Student(String firstName, String lastName, String email,
                    String studentId, Group group) {
        super(firstName, lastName, email);
        setStudentId(studentId);
        setGroup(group);
    }

    /** New student: email is generated from the name. */
    public static Student register(String firstName, String lastName,
                                   String studentId, Group group) {
        return new Student(firstName, lastName,
                PersonUtils.generateEmailFromName(firstName, lastName),
                studentId, group);
    }

    /** Existing student loaded from storage: email is taken as stored. */
    public static Student load(String firstName, String lastName, String email,
                               String studentId, Group group) {
        return new Student(firstName, lastName, email, studentId, group);
    }

    public String getStudentId() { return studentId; }
    public Group getGroup()      { return group; }

    public final void setStudentId(String studentId) {
        this.studentId = StudentUtils.formatStudentId(studentId);
    }

    public final void setGroup(Group group) {
        this.group = Objects.requireNonNull(group, "group can not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return studentId.equals(student.studentId)
                && group.equals(student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studentId, group);
    }

    @Override
    protected String fieldsString() {
        return super.fieldsString() + ", studentId='%s', group=%s"
                .formatted(studentId, group.getFullNumber());
    }
}
