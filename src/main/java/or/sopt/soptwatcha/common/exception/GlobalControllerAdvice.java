package or.sopt.soptwatcha.common.exception;

import lombok.extern.slf4j.Slf4j;
import or.sopt.soptwatcha.common.response.BaseErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[CustomException] {} - {}", errorCode.name(), e.getMessage());

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("입력값이 올바르지 않습니다.");

        log.error("[ValidationException] {}", errorMessage);

        BaseErrorResponse errorResponse = BaseErrorResponse.of(
                false,
                ErrorCode.INVALID_PARAMETER.getStatusCode(),
                errorMessage
        );

        return ResponseEntity
                .status(ErrorCode.INVALID_PARAMETER.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler({
            BindException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<BaseErrorResponse> handleBadRequestException(Exception e) {
        log.error("[BadRequest] {}", e.getMessage());

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getHttpStatus())
                .body(BaseErrorResponse.of(ErrorCode.INVALID_REQUEST));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("[MethodNotAllowed] {}", e.getMessage());

        return ResponseEntity
                .status(ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(BaseErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("[ApiNotFound] {}", e.getMessage());

        return ResponseEntity
                .status(ErrorCode.API_NOT_FOUND.getHttpStatus())
                .body(BaseErrorResponse.of(ErrorCode.API_NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseErrorResponse> handleException(Exception e) {
        log.error("[InternalServerError] ", e);

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(BaseErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

}