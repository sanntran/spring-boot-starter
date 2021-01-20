package net.ionoff.common.validation.constant;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Reflections {

    public static List<String> acceptedStrings() {
        return Arrays.asList("A", "B", "C");
    }

    public static List<BigDecimal> notAcceptedNumbers() {
        return Arrays.asList(BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3));
    }

    public static List<BigDecimal> acceptedNumbers() {
        return Arrays.asList(BigDecimal.valueOf(-2), BigDecimal.valueOf(-1), BigDecimal.valueOf(0));
    }

    public static List<String> notAcceptedStrings() {
        return Arrays.asList("X", "Y", "Z");
    }

}
