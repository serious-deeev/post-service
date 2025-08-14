package org.serious.dev.exception;

import static org.serious.dev.exception.GrpcErrorCode.POST_RESERVATION_ERROR;

public class PostReservationException extends RuntimeException implements HasGrpcErrorCode {

    private static final GrpcErrorCode ERROR_TYPE = POST_RESERVATION_ERROR;
    private static final String TEMPLATE = "неуспешная попытка резервирования поста id=%d пользователя userId=%d";

    public PostReservationException(Long id, Long userId, Exception e) {
        super(String.format(TEMPLATE, id, userId), e);
    }

    @Override
    public GrpcErrorCode getErrorType() {
        return ERROR_TYPE;
    }
}
