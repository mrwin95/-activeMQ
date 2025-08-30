package activemq.notificationsvc.messaging;

import activemq.notificationsvc.config.JmsTopicConfig;
import activemq.notificationsvc.domain.OrderCreatedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationListener.class);

    @JmsListener(destination = JmsTopicConfig.NOTIFICATIONS_TOPIC, subscription = "${app.jms.subscription_name}", containerFactory = "topicJmsListenerContainerFactory")
    public void onMessage(OrderCreatedMessage msg) {
        LOG.info("[Notification] orderId={}, customer={}, total={}",
                msg.orderId(), msg.customerName(), msg.total());
    }
}
