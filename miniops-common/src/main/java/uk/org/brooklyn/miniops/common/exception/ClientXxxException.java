package uk.org.brooklyn.miniops.common.exception;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
public class ClientXxxException extends MiniOpsException {

    public ClientXxxException(String message) {
        super(400, message);
    }

    public ClientXxxException(int code, String message) {
        super(code, message);
    }

    public static ClientXxxException itemAlreadyExists(String itemName) {
        return new ClientXxxException(400, String.format("%s already exists", itemName));
    }

    public static ClientXxxException itemNotFound(String itemName) {
        return new ClientXxxException(404, String.format("%s do(es) not exist", itemName));
    }
}
