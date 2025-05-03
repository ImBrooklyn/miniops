package uk.org.brooklyn.miniops.confer.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ImBrooklyn
 * @since 03/05/2025
 */
@AllArgsConstructor
@Getter
public enum MySQLProps implements PropsMapping {
    URL("url", "spring.datasource.url", false),

    USERNAME("username", "spring.datasource.username", false),

    PASSWORD("password", "spring.datasource.password", true),

    DRIVER_CLASS_NAME("driver-class-name", "spring.datasource.driver-class-name", false),
    ;

    private final String prop;

    private final String springProp;

    private final boolean secret;
}
