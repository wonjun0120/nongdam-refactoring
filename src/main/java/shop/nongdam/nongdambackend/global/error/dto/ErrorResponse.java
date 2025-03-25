package shop.nongdam.nongdambackend.global.error.dto;

public record ErrorResponse(
        int statusCode,
        String message
) {
}