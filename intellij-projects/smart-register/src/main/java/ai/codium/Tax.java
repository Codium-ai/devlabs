package ai.codium;

import gov.data.Rates;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Tax {

    public BigDecimal calculateStateTax(String state, BigDecimal price) {
        Rates rate;
        try {
            rate = Rates.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid state code: " + state);
        }
        BigDecimal taxRate = rate.getTaxRate();
        BigDecimal tax = price.multiply(taxRate);
        return tax.setScale(2, RoundingMode.HALF_UP);
    }
}

