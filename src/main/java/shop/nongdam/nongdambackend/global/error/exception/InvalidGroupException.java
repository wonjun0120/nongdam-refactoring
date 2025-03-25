package shop.nongdam.nongdambackend.global.error.exception;

public abstract class InvalidGroupException extends RuntimeException{
    public InvalidGroupException(String message) {
        super(message);
    }
}
