package activemq.notificationsvc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class JmsStartupLog {

    private static final Logger LOG = LoggerFactory.getLogger(JmsStartupLog.class);

    @Value("${app.jms.client_id:}")
    private String clientId;

    @Value("${app.jms.subscription_name:}")
    private String subscription;

    @Value("${spring.jms.cache.enabled:false}")
    private boolean cacheEnabled;

    @EventListener(ApplicationReadyEvent.class)
    public void logIds(){
        LOG.info("JMS durable IDs -> clientId='{}', subscriptionName='{}' (cacheEnabled={})", clientId, subscription, cacheEnabled);
    }
}
