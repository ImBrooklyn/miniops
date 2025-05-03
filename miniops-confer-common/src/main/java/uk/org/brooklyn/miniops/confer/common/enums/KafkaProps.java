package uk.org.brooklyn.miniops.confer.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ImBrooklyn
 * @since 03/05/2025
 */
@AllArgsConstructor
@Getter
public enum KafkaProps implements PropsMapping {
    BOOTSTRAP_SERVERS("bootstrap-servers", "spring.kafka.bootstrap-servers", false),
    ;

    private final String prop;

    private final String springProp;

    private final boolean secret;
}
