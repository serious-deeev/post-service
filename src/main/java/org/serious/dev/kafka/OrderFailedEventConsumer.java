package org.serious.dev.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.serious.dev.kafka.event.OrderFailedEvent;
import org.serious.dev.service.PostService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFailedEventConsumer {

    private final PostService postService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${spring.kafka.topics.order-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "orderFailedKafkaListenerContainerFactory"
    )
    public void listenPostServiceConsumerGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        Optional<OrderFailedEvent> optionalEvent = getOptionalEvent(record);
        if (optionalEvent.isEmpty()) {
            ack.acknowledge();
            return;
        }

        OrderFailedEvent orderFailedEvent = optionalEvent.get();
        String requestId = orderFailedEvent.getRequestId();
        Long postId = orderFailedEvent.getPostId();

        log.info("[{}] получено событие для отмены резервирования поста с postId={})", requestId, postId);
        postService.cancelPostReservation(postId, orderFailedEvent.getUserId());
        log.info("[{}] выполнена отмена резервирования поста с postId={}", requestId, postId);
        ack.acknowledge();
    }

    private Optional<OrderFailedEvent> getOptionalEvent(ConsumerRecord<String, String> record) {
        try {
            return Optional.ofNullable(objectMapper.readValue(record.value(), OrderFailedEvent.class));
        } catch (JsonProcessingException e) {
            log.warn("Неуспешная попытка десериализации события в OrderFailedEvent: {}", record.value(), e);
            return Optional.empty();
        }
    }
}
