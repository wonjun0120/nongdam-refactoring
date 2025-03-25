package shop.nongdam.nongdambackend.farm.exception;

import shop.nongdam.nongdambackend.global.error.exception.InvalidGroupException;

public class FarmAlreadyExistException extends InvalidGroupException {
    public FarmAlreadyExistException(String message) {
        super(message);
    }

    public FarmAlreadyExistException() {
        this("Error: 이미 등록한 농산물 판매자입니다.");
    }
}
