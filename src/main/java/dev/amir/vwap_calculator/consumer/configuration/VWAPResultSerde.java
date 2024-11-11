
package dev.amir.vwap_calculator.consumer.configuration;


import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class VWAPResultSerde extends Serdes.WrapperSerde<AggregateResult> {
    public VWAPResultSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(AggregateResult.class));
    }
}



