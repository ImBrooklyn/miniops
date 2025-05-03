package uk.org.brooklyn.miniops.common.exception;

import lombok.Getter;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Getter
public class MiniOpsException extends RuntimeException {

    private final int code;

    public MiniOpsException(int code, String message) {
        super(message);
        this.code = code;
    }
}
