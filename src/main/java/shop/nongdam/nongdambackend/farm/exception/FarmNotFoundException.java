package shop.nongdam.nongdambackend.farm.exception;

import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class FarmNotFoundException extends NotFoundGroupException{
    public FarmNotFoundException(String message){
        super(message);
    }

    public FarmNotFoundException(){
        this("Error: 농가를 찾을 수 없습니다.");
    }
}
