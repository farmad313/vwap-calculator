package dev.amir.vwap_calculator.finalcalculator.resource;


import dev.amir.vwap_calculator.finalcalculator.data.AggregateResultEntity;
import dev.amir.vwap_calculator.finalcalculator.data.VwapDto;
import dev.amir.vwap_calculator.finalcalculator.service.AggregateResultService;
import dev.amir.vwap_calculator.finalcalculator.service.CalculatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/aggregateresult")
public class AggregateResultController {

    AggregateResultService aggregateResultService;
    CalculatorService calculatorService;

    AggregateResultController(AggregateResultService aggregateResultService, CalculatorService calculatorService) {
        this.aggregateResultService = aggregateResultService;
        this.calculatorService = calculatorService;
    }

    @GetMapping
    public List<AggregateResultEntity> getAll() {
        return aggregateResultService.getAllAggregateResult();
    }

    @GetMapping("/recent")
    public List<AggregateResultEntity> getRecent(@RequestParam(required = false, defaultValue = "60") int minutes) {
        LocalDateTime timeThreshold = LocalDateTime.now().minusMinutes(minutes);
        return aggregateResultService.getRecentAggregateResult(timeThreshold);
    }


    @GetMapping("/recent/vwap")
    public List<VwapDto> getRecentVwap(@RequestParam(required = false, defaultValue = "60") int minutes) {
        LocalDateTime timeThreshold = LocalDateTime.now().minusMinutes(minutes);
        return calculatorService.calcVwapByTimestampAfter(timeThreshold);
    }

}