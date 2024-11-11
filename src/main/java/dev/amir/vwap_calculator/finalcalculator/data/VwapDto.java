package dev.amir.vwap_calculator.finalcalculator.data;

import java.math.BigDecimal;

public record VwapDto(String currencyPair, BigDecimal vwap) {
}
