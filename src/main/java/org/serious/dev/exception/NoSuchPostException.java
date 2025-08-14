package org.serious.dev.exception;

import static org.serious.dev.exception.GrpcErrorCode.POST_NOT_FOUND;

public class NoSuchPostException extends RuntimeException implements HasGrpcErrorCode {

    private static final GrpcErrorCode ERROR_TYPE = POST_NOT_FOUND;
    private static final String TEMPLATE = "пост id=%d не найден";

    public NoSuchPostException(Long id) {
        super(String.format(TEMPLATE, id));
    }

    @Override
    public GrpcErrorCode getErrorType() {
        return ERROR_TYPE;
    }
}
