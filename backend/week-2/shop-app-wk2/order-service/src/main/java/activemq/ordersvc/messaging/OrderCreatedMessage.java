package activemq.ordersvc.messaging;

import java.io.Serializable;
import java.math.BigDecimal;

public record OrderCreatedMessage(Long orderId, String customerName, BigDecimal total) implements Serializable {}
