package ua.university.model;

import java.util.Objects;

public class Course {

    private Subject subject;
    private Teacher teacher;
    private Group group;

    public Course(Subject subject, Teacher teacher, Group group) {
        setSubject(subject);
        setTeacher(teacher);
        setGroup(group);
    }

    public static Course of(Subject subject, Teacher teacher, Group group) {
        return new Course(subject, teacher, group);
    }

    public Subject getSubject() { return subject; }
    public Teacher getTeacher() { return teacher; }
    public Group getGroup()     { return group; }

    public final void setSubject(Subject subject) {
        this.subject = Objects.requireNonNull(subject, "subject can not be null");
    }

    public final void setTeacher(Teacher teacher) {
        this.teacher = Objects.requireNonNull(teacher, "teacher can not be null");
    }

    public final void setGroup(Group group) {
        this.group = Objects.requireNonNull(group, "group can't be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return subject.equals(course.subject)
                && teacher.equals(course.teacher)
                && group.equals(course.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, teacher, group);
    }

    @Override
    public String toString() {
        return "Course{subject=%s, teacher=%s, group=%s}"
                .formatted(subject.getTitle(), teacher.getFullName(), group.getFullNumber());
    }
}
