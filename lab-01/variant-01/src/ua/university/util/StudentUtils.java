package ua.university.util;

import java.util.Locale;

public final class StudentUtils {

    /** Format: 2 letters + 8 digits, e.g. ST00012345. */
    static final String STUDENT_ID_PATTERN = "[A-Z]{2}\\d{8}";

    private StudentUtils() {
    }

    /** " st12345 " -> "ST00012345" (pads the numeric part with zeros to 8 digits). */
    public static String formatStudentId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("studentId can not be empty");
        }
        String cleaned = id.strip().toUpperCase(Locale.ROOT);

        if (ValidationHelper.isStringMatchPattern(cleaned, "[A-Z]{2}\\d{1,8}")) {
            String prefix = cleaned.substring(0, 2);
            String digits = cleaned.substring(2);
            cleaned = prefix + "0".repeat(8 - digits.length()) + digits;
        }

        if (!ValidationHelper.isStringMatchPattern(cleaned, STUDENT_ID_PATTERN)) {
            throw new IllegalArgumentException("studentId must be 2 letters + up to 8 digits, got: '" + id + "'");
        }
        return cleaned;
    }
}
