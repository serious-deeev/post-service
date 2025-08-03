package org.serious.dev.exception;

import static org.serious.dev.exception.ErrorCode.POST_CANCEL_RESERVATION_ERROR;

public class PostCancelException extends RuntimeException implements HasErrorCode {

    private static final ErrorCode ERROR_TYPE = POST_CANCEL_RESERVATION_ERROR;
    private static final String TEMPLATE = "неуспешная попытка отмены резерва поста id=%d пользователя userId=%d";

    public PostCancelException(Long id, Long userId, Exception e) {
        super(String.format(TEMPLATE, id, userId), e);
    }

    @Override
    public Enum<?> getErrorType() {
        return ERROR_TYPE;
    }
}
