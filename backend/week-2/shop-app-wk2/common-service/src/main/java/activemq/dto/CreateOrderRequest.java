package activemq.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(String id, String customerName, BigDecimal total) {
}
