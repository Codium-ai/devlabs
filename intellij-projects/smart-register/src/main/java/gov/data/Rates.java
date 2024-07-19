package gov.data;

import java.math.BigDecimal;

public enum Rates {
    TX {
        public BigDecimal getTaxRate() {
            return new BigDecimal(0.0825);
        }
    },
    CA {
        public BigDecimal getTaxRate() {
            return new BigDecimal(0.0725);
        }
    }, PA {
        public BigDecimal getTaxRate() {
            return new BigDecimal(0.060);
        }
    }, DE {
        public BigDecimal getTaxRate() {
            return new BigDecimal(0.0);
        }
    };

    public abstract BigDecimal getTaxRate();
}
