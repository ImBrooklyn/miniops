package uk.org.brooklyn.miniops.common.exception;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
public class ServerXxxException extends MiniOpsException {

    public ServerXxxException(String message) {
        super(500, message);
    }

    public ServerXxxException(int code, String message) {
        super(code, message);
    }
}
