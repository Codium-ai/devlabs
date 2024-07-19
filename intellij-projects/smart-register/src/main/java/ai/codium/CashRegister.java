package ai.codium;

import java.math.BigDecimal;

public class CashRegister {

    private final Tax taxCalculator;

    // Constructor injection for IOC
    public CashRegister(Tax taxCalculator) {
        this.taxCalculator = taxCalculator;
    }

    public BigDecimal calculateFinalPrice(String state, BigDecimal price, BigDecimal discountPercentage) {
        BigDecimal discountAmount = price.multiply(discountPercentage).divide(new BigDecimal("100"));
        BigDecimal discountedPrice = price.subtract(discountAmount);
        BigDecimal tax = taxCalculator.calculateStateTax(state, discountedPrice);
        return discountedPrice.add(tax);
    }
}