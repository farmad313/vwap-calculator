
package dev.amir.vwap_calculator.consumer.service;

import dev.amir.vwap_calculator.consumer.data.PriceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * This class is not used in the application and is just for testing purposes.
 * It consumes messages from the Kafka topic one by one.
 * Our application uses the KafkaStreams API to consume messages from the Kafka topic.
 * autoStartup = "false" is used to prevent the application from consuming messages from the Kafka topic.
 */
@Slf4j
@Component
public class KafkaMessageConsumer {

    @KafkaListener(topics = "myTopic", groupId = "myGroupId", autoStartup = "false")
    public void priceDataConsumer(PriceData priceData) {
        log.info(">>> Consumed from Kafka: payload:({})", priceData);
    }
}

