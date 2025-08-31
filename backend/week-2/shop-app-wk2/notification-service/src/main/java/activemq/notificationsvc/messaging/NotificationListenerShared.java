package activemq.notificationsvc.messaging;

import activemq.dto.OrderCreatedMessage;
import activemq.notificationsvc.config.JmsTopicConfig;
//import org.apache.activemq.artemis.api.core.Message;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(value = "app.jms.mode", havingValue = "shared")
@Component
public class NotificationListenerShared {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationListenerShared.class);

    @JmsListener(destination = JmsTopicConfig.NOTIFICATIONS_TOPIC,
    subscription = "${app.jms.shared.subscription_name}",
    containerFactory = "sharedListenerFactory")
    public void onShared(OrderCreatedMessage msg, Message jms){
        String instance = System.getenv().getOrDefault("HOSTNAME", "local");
        LOG.info("[shared: {}] order id={} customer={} total={}", instance, msg.orderId(), msg.customerName(), msg.total());
    }
}
