package dev.amir.vwap_calculator.consumer.configuration;

import dev.amir.vwap_calculator.consumer.data.PriceData;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class PriceDataSerde extends Serdes.WrapperSerde<PriceData> {
    public PriceDataSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(PriceData.class));
    }
}
