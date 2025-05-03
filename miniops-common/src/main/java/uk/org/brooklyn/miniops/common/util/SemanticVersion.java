package uk.org.brooklyn.miniops.common.util;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Data
public class SemanticVersion implements Serializable {

    private SemanticVersion(Integer major,
                            Integer minor,
                            Integer patch,
                            String prerelease,
                            String buildMetadata) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.prerelease = prerelease;
        this.buildMetadata = buildMetadata;
    }

    private int major;

    private int minor;

    private int patch;

    private String prerelease;

    private String buildMetadata;

    private static final String REGEXP = "^(?<major>0|[1-9]\\d*)\\.(?<minor>0|[1-9]\\d*)\\.(?<patch>0|[1-9]\\d*)(?:-(?<prerelease>(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+(?<buildMetadata>[0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$";

    private static final Pattern PATTERN = Pattern.compile(REGEXP);

    public static boolean isValid(String version) {
        Objects.requireNonNull(version);
        Matcher matcher = PATTERN.matcher(version);
        return matcher.matches();
    }

    public static SemanticVersion parse(String version) {
        Objects.requireNonNull(version);
        Matcher matcher = PATTERN.matcher(version);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("invalid semantic version: %s", version));
        }

        return new SemanticVersion(
                Integer.parseInt(matcher.group("major")),
                Integer.parseInt(matcher.group("minor")),
                Integer.parseInt(matcher.group("patch")),
                StringUtils.defaultIfBlank(matcher.group("prerelease"), ""),
                StringUtils.defaultIfBlank(matcher.group("buildMetadata"), "")
        );
    }
}
