package activemq.ordersvc.messaging;

import activemq.ordersvc.config.JmsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class OrderCreatedListener {

    private static final Logger LOG = LoggerFactory.getLogger(OrderCreatedListener.class);

    @JmsListener(destination = JmsConfig.ORDER_CREATED_QUEUE, containerFactory = "createFactory")
    public void handleOrderCreated(OrderCreatedMessage orderCreatedMessage) {
        LOG.info("Received OrderCreatedMessage: Id={}, customerName={}, total={}", orderCreatedMessage.orderId(), orderCreatedMessage.customerName(), orderCreatedMessage.total());
    }
}
