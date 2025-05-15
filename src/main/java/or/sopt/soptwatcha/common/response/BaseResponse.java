package or.sopt.soptwatcha.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

@JsonPropertyOrder({"success", "code", "message", "data"})
public class BaseResponse<T> {
    private final Boolean success;
    private final int code;
    private final String message;
    private final T data;

    private BaseResponse(T data) {
        this.success = true;
        this.code = HttpStatus.OK.value();
        this.message = "요청에 성공하였습니다.";
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(data);
    }

}