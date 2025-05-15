package or.sopt.soptwatcha.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import or.sopt.soptwatcha.common.exception.ErrorCode;

import java.time.LocalDateTime;

@JsonPropertyOrder({"success", "code", "message", "timestamp"})
public class BaseErrorResponse {
    private final boolean success;
    private final int code;
    private final String message;
    private final LocalDateTime timestamp;

    public BaseErrorResponse(boolean success, int code, String message, LocalDateTime timestamp) {
        this.success = false;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    public BaseErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
        this.success = false;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}