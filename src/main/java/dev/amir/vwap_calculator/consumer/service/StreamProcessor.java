package dev.amir.vwap_calculator.consumer.service;

import dev.amir.vwap_calculator.consumer.configuration.AggregateResult;
import dev.amir.vwap_calculator.consumer.configuration.VWAPResultSerde;
import dev.amir.vwap_calculator.consumer.data.PriceData;
import dev.amir.vwap_calculator.finalcalculator.data.AggregateResultEntity;
import dev.amir.vwap_calculator.finalcalculator.repository.AggregateResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

import static dev.amir.vwap_calculator.utility.Utility.getLocalDateTime;
import static dev.amir.vwap_calculator.utility.Utility.logResult;

@Service
@Slf4j
public class StreamProcessor {

    @Value("${intermediate-processor.window-duration-minutes}")
    private int windowDurationMinutes;


    private final AggregateResultRepository aggregateResultRepository;

    public StreamProcessor(AggregateResultRepository aggregateResultRepository) {
        this.aggregateResultRepository = aggregateResultRepository;
    }


    public void doProcess(KStream<String, PriceData> stream) {
        KGroupedStream<String, PriceData> groupedStream = getGroupedStreamByCurrencyPair(stream);
        KTable<Windowed<String>, AggregateResult> vwapTable = getAggregateVwapAndTotalVolume(groupedStream);
        saveAggregateTable(vwapTable);
    }


    private void saveAggregateTable(KTable<Windowed<String>, AggregateResult> vwapTable) {
        logResult("INTERMEDIATE AGGREGATOR SAVED ENTITIES TO DB AT [" + getLocalDateTime(Instant.now(), "Australia/Sydney") + "] :", new StringBuilder(""));

        vwapTable.toStream()
                .foreach((key, value) -> {
                    AggregateResultEntity entity = new AggregateResultEntity();
                    entity.setTimestamp(getLocalDateTime(key.window().startTime(), "Australia/Sydney"));
                    entity.setCurrencyPair(key.key());
                    entity.setTotalVolume(value.getTotalVolume());
                    entity.setVwap(value.getVwap());

                    aggregateResultRepository.save(entity);
                    log.info("+++ Saved to DB: {}", entity);
                });
    }


    private KTable<Windowed<String>, AggregateResult> getAggregateVwapAndTotalVolume(KGroupedStream<String, PriceData> groupedStream) {
        return groupedStream
                .windowedBy(TimeWindows.of(Duration.ofMinutes(windowDurationMinutes)))
                .aggregate(
                        AggregateResult::new,
                        (key, value, aggregate) -> {
                            aggregate.addPriceData(value);
                            return aggregate;
                        },
                        Materialized.with(Serdes.String(), new VWAPResultSerde())
                );
    }


    static KGroupedStream<String, PriceData> getGroupedStreamByCurrencyPair(KStream<String, PriceData> stream) {
        return stream.groupBy((key, value) -> value.currencyPair());
    }

}
