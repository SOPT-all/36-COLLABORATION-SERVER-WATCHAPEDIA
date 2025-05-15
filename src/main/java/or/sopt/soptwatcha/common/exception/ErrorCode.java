package or.sopt.soptwatcha.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //common
    METHOD_NOT_ALLOWED(100, HttpStatus.METHOD_NOT_ALLOWED.value(), "유효하지 않은 Http 메서드입니다."),
    API_NOT_FOUND(101, HttpStatus.NOT_FOUND.value(), "존재하지 않는 API 입니다."),
    INTERNAL_SERVER_ERROR(102, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류입니다."),
    ;

    private final int code;
    private final int httpStatus;
    private final String message;

    ErrorCode(int code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}