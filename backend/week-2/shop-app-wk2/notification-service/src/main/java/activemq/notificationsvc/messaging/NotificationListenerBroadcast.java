package activemq.notificationsvc.messaging;

import activemq.dto.OrderCreatedMessage;
import activemq.notificationsvc.config.JmsTopicConfig;
import org.apache.activemq.artemis.api.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListenerBroadcast {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationListenerBroadcast.class);

    @JmsListener(destination = JmsTopicConfig.NOTIFICATIONS_TOPIC,
    subscription = "${app.jms.broadcast.subscription_name}", //unique per instance
    containerFactory = "broadcastListenerFactory")
    public void onMessage(OrderCreatedMessage msg, Message jms){
        LOG.info("[broadcast] order id = {} customer={} total={}", msg.orderId(), msg.customerName(), msg.total());
    }
}
