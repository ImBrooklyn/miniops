package uk.org.brooklyn.miniops.confer.client.util;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
public final class StringUtils {
    private StringUtils() {
    }

    public static final String EMPTY = "";

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

        }
        return true;
    }
}
