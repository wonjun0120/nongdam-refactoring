package shop.nongdam.nongdambackend.farm.exception;

import shop.nongdam.nongdambackend.global.error.exception.AccessDeniedGroupException;

public class FarmAccessDeniedException extends AccessDeniedGroupException {
    public FarmAccessDeniedException(String message){
        super(message);
    }

    public FarmAccessDeniedException(){
        this("Error: 농산물 판매자 권한이 없습니다");
    }
}
