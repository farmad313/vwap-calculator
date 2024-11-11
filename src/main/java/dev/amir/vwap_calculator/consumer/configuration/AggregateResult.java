

package dev.amir.vwap_calculator.consumer.configuration;

import dev.amir.vwap_calculator.consumer.data.PriceData;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;


public class AggregateResult implements Serializable {
    private BigDecimal totalPriceVolume;
    @Getter
    private BigDecimal totalVolume;
    @Getter
    private BigDecimal vwap;

    public AggregateResult() {
        this.vwap = BigDecimal.ZERO;
        this.totalPriceVolume = BigDecimal.ZERO;
        this.totalVolume = BigDecimal.ZERO;
    }


    public void addPriceData(PriceData priceData) {
        BigDecimal price = new BigDecimal(priceData.price());
        BigDecimal volume = new BigDecimal(priceData.volume());
        this.totalPriceVolume = this.totalPriceVolume.add(price.multiply(volume));
        this.totalVolume = this.totalVolume.add(volume);
        this.vwap = calculateVWAP();
    }

    public BigDecimal calculateVWAP() {
        return totalPriceVolume.divide(totalVolume, BigDecimal.ROUND_HALF_UP);
    }

}

