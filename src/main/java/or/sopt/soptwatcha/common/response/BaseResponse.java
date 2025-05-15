package or.sopt.soptwatcha.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"success", "code", "message", "data"})
public class BaseResponse<T> {
    private final Boolean success;
    private final int code;
    private final String message;
    private final T data;

    private BaseResponse(Boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(true, HttpStatus.OK.value(), "요청에 성공하였습니다.", data);
    }

    public static <T> BaseResponse<T> ok(String message, T data) {
        return new BaseResponse<>(true, HttpStatus.OK.value(), message, data);
    }

    public static <T> BaseResponse<T> created(T data) {
        return new BaseResponse<>(true, HttpStatus.CREATED.value(), "리소스가 생성되었습니다.", data);
    }

    public static BaseResponse<?> noContent() {
        return new BaseResponse<>(true, HttpStatus.NO_CONTENT.value(), "요청을 처리했습니다.", null);
    }
}