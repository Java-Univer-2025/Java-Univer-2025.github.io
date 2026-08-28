package ua.university.model;

import java.util.Objects;
import ua.university.util.PersonUtils;

public class Teacher extends Person {

    private String department;
    private String position;

    public Teacher(String firstName, String lastName, String email,
                   String department, String position) {
        super(firstName, lastName, email);
        setDepartment(department);
        setPosition(position);
    }

    public static Teacher register(String firstName, String lastName,
                             String department, String position) {
        return new Teacher(firstName, lastName,
                PersonUtils.generateEmailFromName(firstName, lastName),
                department, position);
    }

    public static Teacher load(String firstName, String lastName, String email,
                               String department, String position) {
        return new Teacher(firstName, lastName, email, department, position);
    }

    public String getDepartment() { return department; }
    public String getPosition()   { return position; }

    public final void setDepartment(String department) {
        this.department = department;
    }

    public final void setPosition(String position) {
        this.position = PersonUtils.requireNonBlank(position, "position");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return department.equals(teacher.department)
                && position.equals(teacher.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), department, position);
    }

    @Override
    protected String fieldsString() {
        return super.fieldsString() + ", department='%s', position='%s'"
                .formatted(department, position);
    }
}
