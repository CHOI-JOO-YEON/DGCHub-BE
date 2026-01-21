package com.joo.digimon.util;

public class TextFormattingUtil {

    /**
     * Apply text formatting rules:
     * 1. Add space after 】, ] if not followed by 【, [ or whitespace
     * 2. Add space after 〕 if not already present
     * 3. Remove empty lines
     * 4. Add line break after 》, ., digit before 【, [, 《 (even with space between)
     * 5. Add line break before lines starting with "디지크로스" or "〈룰〉"
     * 6. Add line break after () following 》 if followed by 【, [
     */
    public static String applyTextFormatting(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String formatted = text;

        // Rule 1: Add space after 】 if not followed by 【, [ or whitespace
        formatted = formatted.replaceAll("】(?![【\\[\\s])", "】 ");

        // Rule 1: Add space after ] if not followed by 【, [ or whitespace
        formatted = formatted.replaceAll("\\](?![【\\[\\s])", "] ");

        // Rule 2: Add space after 〕 if not already present
        formatted = formatted.replaceAll("〕(?!\\s)", "〕 ");

        // Rule 4: Add line break after 》, ., digit before 【, [, 《 (with optional space)
        // After 》 followed by 【, [ (with optional space) - NOT 《
        formatted = formatted.replaceAll("》\\s*(?=[【\\[])", "》\n");
        // After . followed by 【, [, 《 (with optional space)
        formatted = formatted.replaceAll("\\.\\s*(?=[【\\[《])", ".\n");
        // After digit followed by 【, [, 《 (with optional space)
        formatted = formatted.replaceAll("([0-9])\\s*(?=[【\\[《])", "$1\n");

        // Rule 5: Add line break before "디지크로스" or "〈룰〉" if not at start
        formatted = formatted.replaceAll("(?<!^)(?<!\\n)(디지크로스)", "\n$1");
        formatted = formatted.replaceAll("(?<!^)(?<!\\n)(〈룰〉)", "\n$1");

        // Rule 6: Add line break after () following 》 if followed by 【, [
        // Pattern: 》...(...) followed by 【 or [
        formatted = formatted.replaceAll("(》[^\\(]*\\([^\\)]*\\))\\s*(?=[【\\[])", "$1\n");

        // Rule 3: Remove empty lines (apply at the end)
        // Remove leading whitespace and newlines at start of lines
        formatted = formatted.replaceAll("(?m)^\\s*[\\r\\n]+", "");
        // Replace multiple consecutive newlines with single newline
        formatted = formatted.replaceAll("[\\r\\n]{2,}", "\n");
        // Trim whitespace at start and end
        formatted = formatted.trim();

        return formatted;
    }

    /**
     * Check if text has any of the formatting patterns
     */
    public static boolean hasFormattingPatterns(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        return text.contains("】") || text.contains("]") || text.contains("〕");
    }

    /**
     * Check if formatting would change the text
     */
    public static boolean needsFormatting(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        String formatted = applyTextFormatting(text);
        return !text.equals(formatted);
    }
}
