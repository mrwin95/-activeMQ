package activemq.notificationsvc.messaging;

import activemq.dto.OrderCreatedMessage;
import activemq.notificationsvc.config.JmsTopicConfig;
import org.apache.activemq.artemis.api.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationListener.class);

    @JmsListener(destination = JmsTopicConfig.NOTIFICATIONS_TOPIC,
            subscription = "${app.jms.subscription_name}",
            containerFactory = "topicListenerFactory")
    public void onMessage(OrderCreatedMessage msg, Message jms) throws Exception {
        int attempt = jms.getIntProperty("JMSXDeliveryCount"); // 1 on first try
        String key = jms.getStringProperty("eventKey"); // if producer sets it
        try {
            // -- do the work --
            LOG.info("consume orderCreated key={} attempt={} id={} customer={} total={}",
                    key, attempt, msg.orderId(), msg.customerName(), msg.total());

//            LOG.info("[Notification] orderId={}, customer={}, total={}",
//                    msg.orderId(), msg.customerName(), msg.total());
        }catch (IllegalArgumentException bad){
            LOG.warn("poison message (non-recoverable), sending to DLQ now. key={}", key, bad);
            throw bad;
        }catch (Exception e){
            if (attempt >= 5) {
                LOG.error("giving up after {} attempts; will DLQ. key={}", attempt, key, e);
            }else {
                LOG.warn("transient failure (attempt {}), will redeliver. key={}",attempt, key, e);
            }
            throw e;
        }

//        catch (NonRecoverableInputException bad) {
//            quarantinePublisher.park(msg); // send copy to quarantine
//            return;                        // ACK original (no redelivery)
//        }
    }
}
