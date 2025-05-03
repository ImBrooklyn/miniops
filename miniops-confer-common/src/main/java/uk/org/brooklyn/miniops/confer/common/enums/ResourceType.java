package uk.org.brooklyn.miniops.confer.common.enums;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
public enum ResourceType {
    MYSQL,
    REDIS,
    KAFKA,
    ;

    private static final Map<ResourceType, Set<PropsMapping>> PROP_KEY_MAP;

    static {
        PROP_KEY_MAP = Map.of(
                MYSQL, Arrays.stream(MySQLProps.values()).collect(Collectors.toSet()),
                REDIS, Arrays.stream(RedisProps.values()).collect(Collectors.toSet()),
                KAFKA, Arrays.stream(KafkaProps.values()).collect(Collectors.toSet())
        );
    }

    public Set<String> propKeys() {
        return PROP_KEY_MAP.get(this).stream()
                .map(PropsMapping::getProp)
                .collect(Collectors.toSet());
    }

    public Map<String, String> propsMapping() {
        return PROP_KEY_MAP.get(this).stream()
                .collect(Collectors.toMap(PropsMapping::getProp, PropsMapping::getSpringProp));
    }
}
