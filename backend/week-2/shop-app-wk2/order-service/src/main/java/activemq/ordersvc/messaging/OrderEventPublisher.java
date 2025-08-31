package activemq.ordersvc.messaging;

import activemq.dto.OrderCreatedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private static final Logger LOG = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final JmsTemplate topicJmsTemplate;
    public static final String NOTIFICATIONS_TOPIC = "activemq.ordersvc.messaging";


    public OrderEventPublisher(JmsTemplate topicJmsTemplate) {
        this.topicJmsTemplate = topicJmsTemplate;
        this.topicJmsTemplate.setPubSubDomain(true);
    }

    public void publishCreated(OrderCreatedMessage payload){
        String eventKey = "order:" + payload.orderId() + ":v1";
        MessagePostProcessor headers = m -> {
            m.setStringProperty("_type", "orderCreated");
            m.setStringProperty("eventKey", eventKey);
            m.setJMSCorrelationID(payload.orderId().toString());
            return m;
        };

        topicJmsTemplate.convertAndSend(NOTIFICATIONS_TOPIC, payload, headers);
    }
}
