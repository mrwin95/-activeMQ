package activemq.notificationsvc.domain;

import java.math.BigDecimal;

public record OrderCreatedMessage(Long orderId, String customerName, BigDecimal total){}
