package dev.amir.vwap_calculator.finalcalculator.service;

import dev.amir.vwap_calculator.finalcalculator.data.AggregateResultEntity;
import dev.amir.vwap_calculator.finalcalculator.data.VwapDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static dev.amir.vwap_calculator.utility.Utility.getLocalDateTime;
import static dev.amir.vwap_calculator.utility.Utility.logResult;

@Service
@Slf4j
public class CalculatorService {

    private final AggregateResultService aggregateResultService;

    public CalculatorService(AggregateResultService aggregateResultService) {
        this.aggregateResultService = aggregateResultService;
    }


    public List<VwapDto> calcVwapByTimestampAfter(LocalDateTime timestamp) {
        List<AggregateResultEntity> allByTimestampAfter = aggregateResultService.getRecentAggregateResult(timestamp);

        logAllDataNeedToBeProcessed(allByTimestampAfter);

        List<VwapDto> vwapDtoList = allByTimestampAfter.stream()
                .collect(Collectors.groupingBy(AggregateResultEntity::getCurrencyPair))
                .entrySet().stream()
                .map(entry -> {
                    String currencyPair = entry.getKey();

                    BigDecimal totalVolume = entry.getValue().stream()
                            .map(AggregateResultEntity::getTotalVolume)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal totalVwapVolume = entry.getValue().stream()
                            .map(e -> e.getVwap().multiply(e.getTotalVolume()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal vwap = totalVwapVolume.divide(totalVolume, RoundingMode.HALF_UP);
                    return new VwapDto(currencyPair, vwap);
                })
                .toList();

        logFinalVwapResult(vwapDtoList);
        return vwapDtoList;
    }


    private static void logFinalVwapResult(List<VwapDto> vwapDtoList) {
        StringBuilder vwapResult = new StringBuilder();
        vwapDtoList.forEach(vwapDto -> vwapResult.append("| ").append(vwapDto.toString()).append("\n"));

        logResult("FINAL AGGREGATOR GENERATED VWAP FOR EACH CURRENCY PAIR AT [" + getLocalDateTime(Instant.now(), "Australia/Sydney") + "]", vwapResult);
    }


    private static void logAllDataNeedToBeProcessed(List<AggregateResultEntity> allByTimestampAfter) {
        StringBuilder aggregateResult = new StringBuilder();
        allByTimestampAfter.forEach(aggregateResultEntity -> aggregateResult.append("| ").append(aggregateResultEntity.toString()).append("\n"));

        logResult("FINAL AGGREGATOR IS BEING CALCULATING VWAP FOR FOLLOWING RECORDS AT [" + getLocalDateTime(Instant.now(), "Australia/Sydney") + "] :", aggregateResult);
    }


    @Scheduled(fixedRateString = "${final-processor.interval}")
    public void scheduledTask() {
        log.info("+++ Scheduled task executed at {}", LocalDateTime.now());
        calcVwapByTimestampAfter(LocalDateTime.now().minusMinutes(60));
    }
}
