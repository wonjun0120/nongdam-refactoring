package shop.nongdam.nongdambackend.global.error.exception;

public abstract class AccessDeniedGroupException extends RuntimeException{
    public AccessDeniedGroupException(String message) {
        super(message);
    }
}
