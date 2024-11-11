package dev.amir.vwap_calculator.finalcalculator.service;

import dev.amir.vwap_calculator.finalcalculator.data.AggregateResultEntity;
import dev.amir.vwap_calculator.finalcalculator.repository.AggregateResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AggregateResultService {

    AggregateResultRepository repository;

    public AggregateResultService(AggregateResultRepository repository) {
        this.repository = repository;
    }


    public List<AggregateResultEntity> getAllAggregateResult() {
        return repository.findAll();
    }

    public List<AggregateResultEntity> getRecentAggregateResult(LocalDateTime timestamp) {
        return repository.findAllByTimestampAfter(timestamp);
    }

}
