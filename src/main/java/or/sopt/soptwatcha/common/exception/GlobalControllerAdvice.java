package or.sopt.soptwatcha.common.exception;

import lombok.extern.slf4j.Slf4j;
import or.sopt.soptwatcha.common.response.BaseErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static or.sopt.soptwatcha.common.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseErrorResponse> handleCustomExceptions(CustomException e) {
        return new ResponseEntity<>(new BaseErrorResponse(e.getErrorCode()), HttpStatusCode.valueOf(e.getErrorCode().getHttpStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("입력값이 잘못되었습니다.");

        BaseErrorResponse response = new BaseErrorResponse(
                false,
                BAD_REQUEST.value(),
                message
        );

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(BAD_REQUEST.value()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseErrorResponse> handle_HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(METHOD_NOT_ALLOWED.getHttpStatus())
                .body(new BaseErrorResponse(METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseErrorResponse> handle_NoHandlerFoundException(NoHandlerFoundException e){
        return ResponseEntity
                .status(API_NOT_FOUND.getHttpStatus())
                .body(new BaseErrorResponse(API_NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseErrorResponse> handle_InternalError(Exception e) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(new BaseErrorResponse(INTERNAL_SERVER_ERROR));

    }

}