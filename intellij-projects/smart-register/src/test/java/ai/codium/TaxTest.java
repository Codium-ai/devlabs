package ai.codium;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaxTest {

    @Test
    public void testCalculateStateTax_TX() {
        Tax tax = new Tax();
        BigDecimal price = new BigDecimal("100.00");
        BigDecimal expectedTax = new BigDecimal("8.25");
        assertEquals(expectedTax, tax.calculateStateTax("TX", price));
    }

    @Test
    public void testCalculateStateTax_CA() {
        Tax tax = new Tax();
        BigDecimal price = new BigDecimal("100.00");
        BigDecimal expectedTax = new BigDecimal("7.25");
        assertEquals(expectedTax, tax.calculateStateTax("CA", price));
    }

    @Test
    public void testCalculateStateTax_PA() {
        Tax tax = new Tax();
        BigDecimal price = new BigDecimal("100.00");
        BigDecimal expectedTax = new BigDecimal("6.00");
        assertEquals(expectedTax, tax.calculateStateTax("PA", price));
    }

    @Test
    public void testCalculateStateTax_DE() {
        Tax tax = new Tax();
        BigDecimal price = new BigDecimal("100.00");
        BigDecimal expectedTax = new BigDecimal("0.00");
        assertEquals(expectedTax, tax.calculateStateTax("DE", price));
    }

    @Test
    public void testCalculateStateTax_InvalidState() {
        Tax tax = new Tax();
        BigDecimal price = new BigDecimal("100.00");
        assertThrows(IllegalArgumentException.class, () -> {
            tax.calculateStateTax("XX", price);
        });
    }
}