package dev.amir.vwap_calculator.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
@Slf4j
public class Utility {
    public static final String TEXT_BLOCK = """
                            
            ---------------------------------------------------------------------------------------------------------------------
            | {} |
            ---------------------------------------------------------------------------------------------------------------------
            {}
            """;

    public static BigDecimal generateRandomBigDecimal(BigDecimal min, BigDecimal max) {
        double randomValue = ThreadLocalRandom.current().nextDouble(min.doubleValue(), max.doubleValue());
        return BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP);
    }


    public static String chooseRandomElement() {
        String[] array = {"NZD/GBP", "AUD/EUR", "USD/EUR", "USD/JPY"};
        return array[new Random().nextInt(array.length)];
    }

    public static LocalDateTime getLocalDateTime(Instant now, String zoneId) {
        return now.atZone(ZoneId.of(zoneId)).toLocalDateTime();
    }

    public static void logResult(String logTitle, StringBuilder logContents) {
        log.info(TEXT_BLOCK, logTitle, logContents.toString());
    }

}
