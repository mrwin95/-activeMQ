package activemq.notificationsvc.config;

import jakarta.jms.ConnectionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
public class ScalingListenerConfig {

    private static DefaultJmsListenerContainerFactory base(ConnectionFactory cf, MessageConverter mc){
        var f = new DefaultJmsListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setPubSubDomain(true); //topic
        f.setSubscriptionDurable(true); //durable
        f.setSessionTransacted(true); //rollback -> redelivery
        f.setMessageConverter(mc);

        f.setErrorHandler(ex -> LoggerFactory.getLogger("JMS-ERROR").error("Listener failed: {}",ex.getMessage(), ex));
        return  f;
    }

    /** Broadcast: each instance has its own durable subscription (unique name per instance). */
    @Bean(name = "broadcastListenerFactory")
    public DefaultJmsListenerContainerFactory broadcastListenerFactory(ConnectionFactory cf, MessageConverter mc){
        var f = base(cf, mc);
        f.setSubscriptionShared(false);
        f.setConcurrency("1"); //MUST be 1 for non-shared durable topics
        return f;
    }

    @Bean(name = "sharedListenerFactory")
    public DefaultJmsListenerContainerFactory sharedListenerFactory(ConnectionFactory cf, MessageConverter mc){
        var f = base(cf, mc);
        f.setSubscriptionShared(true);
        f.setConcurrency("2-8");
        return f;
    }
}
