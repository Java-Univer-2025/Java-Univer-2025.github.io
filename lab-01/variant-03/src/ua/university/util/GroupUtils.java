package ua.university.util;

import java.time.Year;
import java.util.Locale;
import ua.university.model.Group;

public final class GroupUtils {

    static final int MAX_GROUP_AGE_YEARS = 6;

    static final int MIN_GROUP_NUMBER = 1;
    static final int MAX_GROUP_NUMBER = 20;
    static final int MIN_START_YEAR = 1901;
    static final String SPECIALTY_CODE_PATTERN = "[А-ЯІЇЄҐ]{2,4}";

    private GroupUtils() {
    }

    /** "КН" + 1 + 2021 -> "КН1-21" */
    public static String formatGroupFullNumber(Group group) {
        return "%s%d-%02d".formatted(
                group.getSpecialty(), group.getNumber(), group.getStartYear() % 100);
    }

    public static int requireValidNumber(int number) {
        if (!ValidationHelper.isNumberBetween(number, MIN_GROUP_NUMBER, MAX_GROUP_NUMBER)) {
            throw new IllegalArgumentException(
                    "Group number must be between %d and %d, got: %d"
                            .formatted(MIN_GROUP_NUMBER, MAX_GROUP_NUMBER, number));
        }
        return number;
    }

    public static String requireValidSpecialty(String specialty) {
        String cleaned = specialty == null
                ? null
                : specialty.strip().toUpperCase(Locale.ROOT);
        if (!ValidationHelper.isStringMatchPattern(cleaned, SPECIALTY_CODE_PATTERN)) {
            throw new IllegalArgumentException(
                    "Specialty must be 2-4 uppercase Cyrillic letters (e.g. \"КН\"), got: '%s'"
                            .formatted(specialty));
        }
        return cleaned;
    }

    /** Data invariant: checked always, in the constructor. */
    public static int requireValidStartYear(int startYear) {
        int currentYear = 2026;
        if (!ValidationHelper.isNumberBetween(startYear, MIN_START_YEAR, currentYear)) {
            throw new IllegalArgumentException(
                    "Start year must be between %d and %d, got: %d"
                            .formatted(MIN_START_YEAR, currentYear, startYear));
        }
        return startYear;
    }

    /** Business rule for registering a new group. */
    public static int requireRegistrableStartYear(int startYear) {
        int minAllowed = Year.now().getValue() - MAX_GROUP_AGE_YEARS;
        if (startYear < minAllowed) {
            throw new IllegalArgumentException(
                    "A new group can not be older than %d years (start year >= %d), got: %d"
                            .formatted(MAX_GROUP_AGE_YEARS, minAllowed, startYear));
        }
        return startYear;
    }
}