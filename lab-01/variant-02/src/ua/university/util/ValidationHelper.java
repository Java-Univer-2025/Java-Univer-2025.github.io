package ua.university.util;

/**
 * Package-private helper: an implementation detail of the util package.
 * Invisible outside the package — on purpose: the public API of this
 * package is PersonUtils / StudentUtils / GroupUtils.
 */
public final class ValidationHelper {

    private ValidationHelper() {
        // utility class — no instances needed
    }

    public static boolean isStringMatchPattern(String text, String pattern) {
        return text != null && text.matches(pattern);
    }

    public static boolean isNumberBetween(int number, int min, int max) {
        return number >= min && number <= max;
    }

    public static boolean isStringLengthBetween(String text, int min, int max) {
        return text != null && isNumberBetween(text.length(), min, max);
    }
}
