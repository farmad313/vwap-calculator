package dev.amir.vwap_calculator.consumer.data;

public record PriceData(String timestamp, String currencyPair, String price, String volume) {
}