package dev.amir.vwap_calculator.producer.service;

import dev.amir.vwap_calculator.consumer.data.PriceData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageProducer {

    public static final String TOPIC = "myTopic";

    private final KafkaTemplate<String, PriceData> kafkaTemplate;

    public void sendPriceData(PriceData priceData) {
        String kafkaMessageKey = String.valueOf(UUID.randomUUID());
        kafkaTemplate.send(TOPIC, kafkaMessageKey, priceData);
        log.info(">>> Produced & Sent to Kafka: id= {}, payload= ({})", kafkaMessageKey, priceData);
    }


}
