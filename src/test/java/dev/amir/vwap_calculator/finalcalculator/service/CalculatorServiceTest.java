package dev.amir.vwap_calculator.finalcalculator.service;

import dev.amir.vwap_calculator.finalcalculator.data.AggregateResultEntity;
import dev.amir.vwap_calculator.finalcalculator.data.VwapDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CalculatorServiceTest {

    @Mock
    private AggregateResultService aggregateResultService;

    @InjectMocks
    private CalculatorService calculatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalcVwapByTimestampAfter() {
        LocalDateTime timestamp = LocalDateTime.now().minusMinutes(60);

        AggregateResultEntity entity1 = new AggregateResultEntity();
        entity1.setCurrencyPair("BTC/USD");
        entity1.setTotalVolume(new BigDecimal("300"));
        entity1.setVwap(new BigDecimal("5"));

        AggregateResultEntity entity2 = new AggregateResultEntity();
        entity2.setCurrencyPair("BTC/USD");
        entity2.setTotalVolume(new BigDecimal("200"));
        entity2.setVwap(new BigDecimal("10"));

        List<AggregateResultEntity> entities = Arrays.asList(entity1, entity2);

        when(aggregateResultService.getRecentAggregateResult(timestamp)).thenReturn(entities);

        List<VwapDto> result = calculatorService.calcVwapByTimestampAfter(timestamp);

        double expectedVwap = (double) (5 * 300 + 10 * 200) / (300 + 200);

        assertEquals(1, result.size());
        assertEquals("BTC/USD", result.get(0).currencyPair());
        assertEquals(new BigDecimal(expectedVwap), result.get(0).vwap());
    }
}