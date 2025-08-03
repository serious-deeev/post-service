package org.serious.dev.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.header.Header;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final Environment environment;
    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> orderFailedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();

        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        kafkaListenerContainerFactory.setRecordFilterStrategy(record -> {
            Header typeIdHeader = record.headers()
                    .lastHeader(environment.getProperty("spring.kafka.event-filter.header-key"));
            if (typeIdHeader == null) return true;
            String typeId = new String(typeIdHeader.value(), UTF_8);
            return !typeId.equals(environment.getProperty("spring.kafka.event-filter.expected-value"));
        });
        kafkaListenerContainerFactory.getContainerProperties().setAckMode(MANUAL);

        return kafkaListenerContainerFactory;
    }
}
