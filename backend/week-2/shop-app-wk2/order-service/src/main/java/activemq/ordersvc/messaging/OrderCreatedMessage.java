package activemq.ordersvc.messaging;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * After add Json Converter we can remove {@link Serializable}
 * @param orderId
 * @param customerName
 * @param total
 */
public record OrderCreatedMessage(Long orderId, String customerName, BigDecimal total){}
