package or.sopt.soptwatcha.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import or.sopt.soptwatcha.common.exception.ErrorCode;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"success", "code", "message", "timestamp", "errors"})
public class BaseErrorResponse {
    private final boolean success;
    private final int code;
    private final String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public BaseErrorResponse(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public BaseErrorResponse(ErrorCode errorCode) {
        this(false, errorCode.getStatusCode(), errorCode.getMessage());
    }

    public static BaseErrorResponse of(boolean success, int code, String message) {
        return new BaseErrorResponse(success, code, message);
    }

    public static BaseErrorResponse of(ErrorCode errorCode) {
        return new BaseErrorResponse(errorCode);
    }

}