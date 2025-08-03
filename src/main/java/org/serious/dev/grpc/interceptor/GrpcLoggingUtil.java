package org.serious.dev.grpc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.serious.dev.grpc.PostRequest;
import org.serious.dev.grpc.PostResponse;
import org.serious.dev.grpc.ReservePostRequest;
import org.serious.dev.grpc.ReservePostResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcLoggingUtil {

    <ReqT> void logRequest(ReqT requestMessage, String requestId, String incomingRpc) {
        if (requestMessage instanceof PostRequest request) {
            log.info("[{}] получен grpc-запрос → rpcCall: {}, id поста: {}",
                    requestId,
                    incomingRpc,
                    request.getId());

        } else if (requestMessage instanceof ReservePostRequest request) {
            log.info("[{}] получен grpc-запрос → rpcCall: {}, id поста: {},  id пользователя: {}",
                    requestId,
                    incomingRpc,
                    request.getId(),
                    request.getUserId());

        } else {
            log.info("[{}] неизвестный grpc-вызов: {}", requestId, incomingRpc);
        }
    }

    <RespT> void logResponse(RespT responseMessage, String requestId) {
        if (responseMessage instanceof PostResponse response) {
            log.info(
                    "[{}] найден доступный пост с id = {}, отправляем ответ",
                    requestId,
                    response.getId()
            );

        } else if (responseMessage instanceof ReservePostResponse response) {
            log.info(
                    "[{}] пост успешно зарезервирован",
                    requestId
            );
        }
    }

    void logError(String requestId, Exception exception) {
        log.error(
                "[{}] при обработке запроса возникла ошибка: {}",
                requestId,
                exception.getMessage()
        );
    }
}
