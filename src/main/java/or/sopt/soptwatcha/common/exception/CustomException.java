package or.sopt.soptwatcha.common.exception;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
