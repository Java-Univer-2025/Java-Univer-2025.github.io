package ua.university.util;

import java.util.Locale;

/**
 * Public utilities for names and emails.
 * Internally relies on the package-private ValidationHelper.
 */
public final class PersonUtils {

    /** First/last name: Cyrillic or Latin letters, apostrophe, hyphen; 2..50 chars. */
    static final String NAME_PATTERN = "[\\p{IsCyrillic}\\p{IsLatin}'’ʼ\\-]+";
    static final String EMAIL_PATTERN = "[a-z0-9][a-z0-9._\\-]*@[a-z0-9.\\-]+\\.[a-z]{2,}";

    public static final String EMAIL_DOMAIN = "university.edu.ua";

    private PersonUtils() {
    }

    /** "  іВАН " -> "Іван"; throws IllegalArgumentException for an invalid name. */
    public static String requireValidName(String name, String fieldName) {
        String stripped = name == null ? null : name.strip();
        if (!ValidationHelper.isStringLengthBetween(stripped, 2, 50)
                || !ValidationHelper.isStringMatchPattern(stripped, NAME_PATTERN)) {
            throw new IllegalArgumentException(
                    fieldName + " must be 2-50 letters (Cyrillic or Latin, apostrophe, hyphen allowed), got: '" + name + "'");
        }
        return capitalize(stripped);
    }

    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " can not be empty");
        }
        return value.strip();
    }

    /** "Іван" + "Петренко" -> "Іван Петренко" (with case normalization). */
    public static String formatName(String firstName, String lastName) {
        return capitalize(firstName.strip()) + " " + capitalize(lastName.strip());
    }

    /** Normalizes the email to lower case and validates it. */
    public static String formatEmail(String email) {
        String normalized = email == null
                ? null
                : email.strip().toLowerCase(Locale.ROOT);
        if (!ValidationHelper.isStringMatchPattern(normalized, EMAIL_PATTERN)) {
            throw new IllegalArgumentException("email is not valid: '" + email + "'");
        }
        return normalized;
    }

    /**
     * "Євген" + "Матвієнко" -> "yevhen.matviienko@university.edu.ua".
     * Transliteration follows KMU resolution #55 of 27.01.2010.
     */
    public static String generateEmailFromName(String firstName, String lastName) {
        String local = transliterate(requireValidName(firstName, "firstName"))
                + "."
                + transliterate(requireValidName(lastName, "lastName"));
        return formatEmail(local + "@" + EMAIL_DOMAIN);
    }

    // ─── Transliteration (KMU #55, 2010) ────────────────────────────────────

    /**
     * Transliterates one word (first or last name) according to
     * KMU resolution #55 (2010). Special rules: "зг" -> "zgh";
     * soft sign and apostrophes are not rendered; after a hyphen
     * the word-start rules apply again. Latin letters pass through.
     */
    static String transliterate(String word) {
        String lower = word.toLowerCase(Locale.ROOT);
        String result = "";
        boolean wordStart = true;

        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);

            if (c == 'ь') {
                continue; // not rendered, but do not restart the word
            }
            if (c == '-') {
                wordStart = true; // after a hyphen the next letter starts a word
                continue;
            }

            // "зг" -> "zgh" (to distinguish it from "ж" -> "zh")
            if (c == 'з' && i + 1 < lower.length() && lower.charAt(i + 1) == 'г') {
                result += "zgh";
                i++;
                wordStart = false;
                continue;
            }

            result += mapChar(c, wordStart);
            wordStart = false;
        }
        return result;
    }

    /** One row of the KMU #55 table per case. */
    private static String mapChar(char c, boolean wordStart) {
        switch (c) {
            case 'а': return "a";
            case 'б': return "b";
            case 'в': return "v";
            case 'г': return "h";
            case 'ґ': return "g";
            case 'д': return "d";
            case 'е': return "e";
            case 'ж': return "zh";
            case 'з': return "z";
            case 'и': return "y";
            case 'і': return "i";
            case 'к': return "k";
            case 'л': return "l";
            case 'м': return "m";
            case 'н': return "n";
            case 'о': return "o";
            case 'п': return "p";
            case 'р': return "r";
            case 'с': return "s";
            case 'т': return "t";
            case 'у': return "u";
            case 'ф': return "f";
            case 'х': return "kh";
            case 'ц': return "ts";
            case 'ч': return "ch";
            case 'ш': return "sh";
            case 'щ': return "shch";
            // positional letters: word start vs inside a word
            case 'є': if (wordStart) return "ye"; return "ie";
            case 'ї': if (wordStart) return "yi"; return "i";
            case 'й': if (wordStart) return "y";  return "i";
            case 'ю': if (wordStart) return "yu"; return "iu";
            case 'я': if (wordStart) return "ya"; return "ia";
            default:  return String.valueOf(c); // Latin letters pass through
        }
    }

    /** "іВАН" -> "Іван"; hyphenated names keep both parts capitalized. */
    static String capitalize(String name) {
        String lower = name.toLowerCase(Locale.ROOT);
        String result = "";
        boolean upperNext = true;
        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            if (upperNext) {
                result += Character.toUpperCase(c);
            } else {
                result += c;
            }
            upperNext = (c == '-');
        }
        return result;
    }
}
