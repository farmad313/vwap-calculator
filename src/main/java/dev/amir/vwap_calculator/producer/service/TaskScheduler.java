package dev.amir.vwap_calculator.producer.service;

import dev.amir.vwap_calculator.consumer.data.PriceData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static dev.amir.vwap_calculator.utility.Utility.*;

@Service
public class TaskScheduler {
    private static final BigDecimal MIN = new BigDecimal("0.1");
    private static final BigDecimal MAX = new BigDecimal("10.0");
    public static final String TIME = getLocalDateTime(Instant.now(), "Australia/Sydney").format(DateTimeFormatter.ofPattern("h:mm a"));
    private final KafkaMessageProducer kafkaMessageProducer;

    public TaskScheduler(KafkaMessageProducer kafkaMessageProducer) {
        this.kafkaMessageProducer = kafkaMessageProducer;
    }

    public void task1() {
        kafkaMessageProducer.sendPriceData(getSamplePriceData());
    }

    public void task2() {
        kafkaMessageProducer.sendPriceData(getSamplePriceData());
    }

    public void task3() {
        kafkaMessageProducer.sendPriceData(getSamplePriceData());
    }

    private static PriceData getSamplePriceData() {
        return new PriceData(
                TIME,
                chooseRandomElement(),
                generateRandomBigDecimal(MIN, MAX).toPlainString(),
                generateRandomBigDecimal(MIN, MAX).toPlainString()
        );
    }
}
