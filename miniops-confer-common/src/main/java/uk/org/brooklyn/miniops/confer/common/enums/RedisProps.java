package uk.org.brooklyn.miniops.confer.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ImBrooklyn
 * @since 03/05/2025
 */
@AllArgsConstructor
@Getter
public enum RedisProps implements PropsMapping {
    HOST("host", "spring.data.redis.host", false),

    PORT("port", "spring.data.redis.username", false),

    PASSWORD("password", "spring.data.redis.password", false),

    DATABASE("database", "spring.data.redis.database", false),
    ;

    private final String prop;

    private final String springProp;

    private final boolean secret;
}
