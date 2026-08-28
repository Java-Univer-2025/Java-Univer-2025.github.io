package ua.university.model;

import java.util.Objects;
import ua.university.util.GroupUtils;

public class Group {

    private int number;
    private String specialty;
    private int startYear;

    private Group(int number, String specialty, int startYear) {
        setNumber(number);
        setSpecialty(specialty);
        setStartYear(startYear);
    }

    /** New group: invariant + business rule (not older than 6 years). */
    public static Group register(int number, String specialty, int startYear) {
        GroupUtils.requireRegistrableStartYear(startYear);
        return new Group(number, specialty, startYear);
    }

    /** Existing group loaded from storage: invariant only. */
    public static Group load(int number, String specialty, int startYear) {
        return new Group(number, specialty, startYear);
    }

    public int getNumber()       { return number; }
    public String getSpecialty() { return specialty; }
    public int getStartYear()    { return startYear; }

    /** Derived, never stored: "КН1-21". */
    public String getFullNumber() {
        return GroupUtils.formatGroupFullNumber(this);
    }

    public final void setNumber(int number) {
        this.number = GroupUtils.requireValidNumber(number);
    }

    public final void setSpecialty(String specialty) {
        this.specialty = GroupUtils.requireValidSpecialty(specialty);
    }

    public final void setStartYear(int startYear) {
        this.startYear = GroupUtils.requireValidStartYear(startYear);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return number == group.number
                && startYear == group.startYear
                && specialty == group.specialty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, specialty, startYear);
    }

    @Override
    public String toString() {
        return "Group{fullNumber='%s', specialty='%s', startYear=%d}"
                .formatted(getFullNumber(), specialty, startYear);
    }
}
