package uk.org.brooklyn.miniops.common.exception;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
public class RemoteXxxException extends MiniOpsException{

    public RemoteXxxException(String message) {
        super(400, message);
    }

    public RemoteXxxException(int code, String message) {
        super(code, message);
    }
}
