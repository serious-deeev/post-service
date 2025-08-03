package org.serious.dev.grpc.interceptor;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import lombok.RequiredArgsConstructor;
import org.serious.dev.exception.HasErrorCode;
import org.serious.dev.exception.NoSuchPostException;
import org.serious.dev.exception.PostAlreadyReservedException;
import org.serious.dev.exception.PostCancelException;
import org.serious.dev.exception.PostReservationException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.grpc.Status.FAILED_PRECONDITION;
import static io.grpc.Status.INTERNAL;
import static io.grpc.Status.NOT_FOUND;
import static io.grpc.Status.UNKNOWN;
import static org.serious.dev.grpc.interceptor.GrpcRequestIdInterceptor.REQUEST_ID_KEY;

@Component
@Order(100)
@RequiredArgsConstructor
public class GrpcExceptionInterceptor implements ServerInterceptor {

    private final GrpcLoggingUtil logUtil;

    private static final Map<Class<? extends Exception>, Status> EXCEPTION_STATUS_MAP = Map.of(
            NoSuchPostException.class, NOT_FOUND,
            PostAlreadyReservedException.class, FAILED_PRECONDITION,
            PostReservationException.class, INTERNAL,
            PostCancelException.class, INTERNAL
    );

    private static final Metadata.Key<com.google.rpc.Status> STATUS_KEY =
            ProtoUtils.keyForProto(com.google.rpc.Status.getDefaultInstance());

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        String requestId = REQUEST_ID_KEY.get();
        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);

        return getServerCallListener(call, delegate, requestId);
    }

    private <ReqT, RespT> ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> getServerCallListener(
            ServerCall<ReqT, RespT> call,
            ServerCall.Listener<ReqT> delegate,
            String requestId
    ) {
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception exception) {
                    logUtil.logError(requestId, exception);
                    handleEndpointException(exception, call);
                }
            }
        };
    }

    private <RespT, ReqT> void handleEndpointException(Exception exception, ServerCall<ReqT, RespT> call) {
        Status actualStatus = EXCEPTION_STATUS_MAP.getOrDefault(exception.getClass(), UNKNOWN);
        Metadata metadata = new Metadata();

        if (exception instanceof HasErrorCode errorTypedException) {
            com.google.rpc.Status protoStatus = com.google.rpc.Status.newBuilder()
                    .setMessage(errorTypedException.getErrorType().name())
                    .build();

            metadata.put(STATUS_KEY, protoStatus);
        }

        call.close(
                actualStatus
                        .withCause(exception)
                        .withDescription(exception.getMessage()),
                metadata
        );
    }
}
