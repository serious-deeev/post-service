package org.serious.dev.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OrderFailedEvent {

    private final String requestId;
    private final Long userId;
    private final Long postId;

    @JsonCreator
    public OrderFailedEvent(
            @JsonProperty("requestId") String requestId,
            @JsonProperty("userId") Long userId,
            @JsonProperty("postId") Long postId
    ) {
        this.requestId = requestId;
        this.userId = userId;
        this.postId = postId;
    }
}
