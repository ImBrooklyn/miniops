package uk.org.brooklyn.miniops.confer.common.enums;

/**
 * @author ImBrooklyn
 * @since 03/05/2025
 */
public interface PropsMapping {

    String getProp();

    String getSpringProp();

    boolean isSecret();
}
