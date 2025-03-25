package shop.nongdam.nongdambackend.global.error;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.nongdam.nongdambackend.global.error.dto.ErrorResponse;
import shop.nongdam.nongdambackend.global.error.exception.AccessDeniedGroupException;
import shop.nongdam.nongdambackend.global.error.exception.AuthGroupException;
import shop.nongdam.nongdambackend.global.error.exception.InvalidGroupException;
import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({InvalidGroupException.class})
    public ResponseEntity<ErrorResponse> handleInvalidData(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error: 잘못된 요청입니다.");
        log.error(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthGroupException.class})
    public ResponseEntity<ErrorResponse> handleAuthDate(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error: 인증 오류가 발생했습니다.");
        log.error(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundGroupException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundDate(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Error: 요청한 데이터를 찾을 수 없습니다.");
        log.error(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedGroupException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedDate(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Error: 접근이 거부되었습니다.");
        log.error(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        FieldError fieldError = Objects.requireNonNull(e.getFieldError());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                String.format("Error: %s. (필드: %s)", fieldError.getDefaultMessage(), fieldError.getField()));

        log.error("유효성 검사 오류 - 필드: {} 오류 메시지: {}", fieldError.getField(), fieldError.getDefaultMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
