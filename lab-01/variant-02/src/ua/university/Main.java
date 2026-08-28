package ua.university;

import java.util.HashSet;
import java.util.Set;
import ua.university.model.*;
import ua.university.util.GroupUtils;
import ua.university.util.PersonUtils;

public class Main {

    public static void main(String[] args) {
        // 1. Creating objects: register (new) vs load (from storage)
        Group kn1 = Group.register(1, "кн", 2021);
        Subject java = Subject.of("Object-Oriented Programming (Java)", 6);

        Teacher ivasiuk = Teacher.register("Галина", "Івасюк", "Кафедра ПЗ", "доцент");
        Student matviienko = Student.register("євген", "МАТВІЄНКО", "st42", kn1);
        Student sameStudent = Student.load(
                "Євген", "Матвієнко", "yevhen.matviienko@university.edu.ua",
                "ST00000042", kn1);

        Course oop = Course.of(java, ivasiuk, kn1);

        System.out.println(matviienko);
        System.out.println(ivasiuk);
        System.out.println(oop);

        // 2. Formatting and email generation (KMU-2010 transliteration)
        System.out.println(PersonUtils.formatName("тарас", "ЩЕРБАТЮК"));
        System.out.println(PersonUtils.generateEmailFromName("Тарас", "Щербатюк"));
        System.out.println(PersonUtils.generateEmailFromName("Йосип", "Розгон"));
        System.out.println(GroupUtils.formatGroupFullNumber(kn1)); // КН1-21
        System.out.println(kn1.getFullNumber());                   // same, via the entity

        // 3. equals + hashCode: created differently, still equal
        System.out.println("equals: " + matviienko.equals(sameStudent));
        Set<Student> students = new HashSet<>();
        students.add(matviienko);
        System.out.println("found in HashSet: " + students.contains(sameStudent));

        // 4. Business rule vs data invariant
        Group archived = Group.load(2, "ІПЗ", 2015);   // old but loadable
        System.out.println("loaded: " + archived.getFullNumber());
        try {
            Group.register(2, "ІПЗ", 2015);            // too old to register
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        // 5. Validation: invalid input is rejected
        try {
            Group.register(99, "КН", 2024);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        try {
            Subject.of("Yoga", 99);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        // 6. Access modifiers: package-private ValidationHelper is not visible
        //    from here — uncomment to see the compilation error:
        // ua.university.util.ValidationHelper.isNumberBetween(1, 0, 2);
    }
}