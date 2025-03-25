package shop.nongdam.nongdambackend.region.exception;

import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class RegionNotFoundException extends NotFoundGroupException {
    public RegionNotFoundException(String message) {
        super(message);
    }

    public RegionNotFoundException() {
        this("Error: 존재하지 않는 지역입니다");
    }
}