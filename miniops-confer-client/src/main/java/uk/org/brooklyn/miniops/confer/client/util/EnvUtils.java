package uk.org.brooklyn.miniops.confer.client.util;

import java.util.Set;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
public final class EnvUtils {
    private EnvUtils() {
    }

    private static final Set<String> ON_FLAGS = Set.of("ON", "TRUE", "1");

    public static String getEnvOrDefault(String env, String defaultValue) {
        return StringUtils.defaultIfBlank(System.getenv(env), defaultValue);
    }


    public static String mustGetEnv(String env) {
        String val = System.getenv(env);
        if (StringUtils.isBlank(val)) {
            throw new RuntimeException(String.format("env %s must not be blank", env));
        }

        return val;
    }

    public static boolean getBooleanEnv(String env) {
        String val = getEnvOrDefault(env, "OFF");
        return ON_FLAGS.contains(val);
    }
}
