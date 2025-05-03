package uk.org.brooklyn.miniops.common.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.org.brooklyn.miniops.common.exception.MiniOpsException;
import uk.org.brooklyn.miniops.common.exception.RemoteXxxException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(Map.of("msg", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RemoteXxxException.class)
    public ResponseEntity<?> handleRemoteXxxException(RemoteXxxException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatusCode.valueOf(ex.getCode()));
    }

    @ExceptionHandler(MiniOpsException.class)
    public ResponseEntity<?> handleMiniOpsCmdbException(MiniOpsException ex) {
        return new ResponseEntity<>(Map.of("msg", ex.getMessage()), HttpStatus.valueOf(ex.getCode()));
    }
}
