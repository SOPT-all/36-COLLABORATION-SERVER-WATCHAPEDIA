package or.sopt.soptwatcha.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 클라이언트 오류 - 400번대
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "유효하지 않은 Http 메서드입니다."),
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 API 입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된.파라미터입니다."),

    // 서버 오류 - 500번대
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return httpStatus.value();
    }

}