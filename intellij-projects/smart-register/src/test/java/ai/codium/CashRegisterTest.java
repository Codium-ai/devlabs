package ai.codium;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CashRegisterTest {


    @Test
    public void testCalculateFinalPrice_TX_50PercentDiscount() {
        Tax taxCalculator = new Tax();
        CashRegister cashRegister = new CashRegister(taxCalculator);
        BigDecimal price = new BigDecimal("100.00");
        BigDecimal discountPercentage = new BigDecimal("50");
        BigDecimal expectedFinalPrice = new BigDecimal("54.13"); // Assuming TX tax rate is 8.25%

        BigDecimal finalPrice = cashRegister.calculateFinalPrice("TX", price, discountPercentage);

        assertEquals(expectedFinalPrice, finalPrice);
    }
}