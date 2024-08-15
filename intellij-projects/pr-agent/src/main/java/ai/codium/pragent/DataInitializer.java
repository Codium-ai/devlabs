package ai.codium.pragent;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Bean
    ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(StockRepository stockRepository) {
        return event -> {
            Stock stock = new Stock();
            stock.setId(1L);
            stock.setCompanyName("Example Corp");
            stock.setTickerSymbol("EXMPL");
            stock.setPrice(100.0);
            stockRepository.save(stock);
        };
    }
}