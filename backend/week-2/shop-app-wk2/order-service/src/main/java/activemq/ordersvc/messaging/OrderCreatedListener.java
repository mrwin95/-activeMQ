package activemq.ordersvc.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static activemq.ordersvc.config.JmsConfig.ORDER_CREATED_QUEUE;

@Component
public class OrderCreatedListener {

    private static final Logger LOG = LoggerFactory.getLogger(OrderCreatedListener.class);

    @JmsListener(destination = ORDER_CREATED_QUEUE, containerFactory = "createFactory")
    public void handleOrderCreated(OrderCreatedMessage orderCreatedMessage) {
        LOG.info("Received OrderCreatedMessage: Id={}, customerName={}, total={}", orderCreatedMessage.orderId(), orderCreatedMessage.customerName(), orderCreatedMessage.total());
    }
}
