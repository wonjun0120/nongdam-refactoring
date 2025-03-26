package shop.nongdam.nongdambackend.member.exception;

import shop.nongdam.nongdambackend.global.error.exception.AccessDeniedGroupException;

public class RoleAccessDeniedException extends AccessDeniedGroupException {
        public RoleAccessDeniedException() {
            super("해당 권한으로 접근할 수 없습니다.");
        }

        public RoleAccessDeniedException(String message) {
            super(message);
        }
}
