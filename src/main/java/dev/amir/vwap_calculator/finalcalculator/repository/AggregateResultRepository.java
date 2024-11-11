package dev.amir.vwap_calculator.finalcalculator.repository;

import dev.amir.vwap_calculator.finalcalculator.data.AggregateResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AggregateResultRepository extends JpaRepository<AggregateResultEntity, Long> {
    List<AggregateResultEntity> findAllByTimestampAfter(LocalDateTime timestamp);
}
