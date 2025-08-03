package org.serious.dev.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OrderFailedEvent {

    private final Long userId;
    private final Long postId;

    @JsonCreator
    public OrderFailedEvent(
            @JsonProperty("userId") Long userId,
            @JsonProperty("postId") Long postId
    ) {
        this.userId = userId;
        this.postId = postId;
    }
}
