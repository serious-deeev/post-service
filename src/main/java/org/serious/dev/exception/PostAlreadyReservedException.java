package org.serious.dev.exception;

import static org.serious.dev.exception.GrpcErrorCode.POST_ALREADY_RESERVED;

public class PostAlreadyReservedException extends RuntimeException implements HasGrpcErrorCode {

    private static final GrpcErrorCode ERROR_TYPE = POST_ALREADY_RESERVED;
    private static final String TEMPLATE = "пост id=%d пользователя userId=%d уже был зарезервирован ранее";

    public PostAlreadyReservedException(Long id, Long userId) {
        super(String.format(TEMPLATE, id, userId));
    }

    @Override
    public GrpcErrorCode getErrorType() {
        return ERROR_TYPE;
    }
}
