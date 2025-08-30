package activemq.notificationsvc.config;

import jakarta.jms.ConnectionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
public class JmsListenerTuning {

    @Bean(name = "topicListenerFactory")
    public DefaultJmsListenerContainerFactory topicListenerFactory(ConnectionFactory cf, MessageConverter mc){
        var f = new DefaultJmsListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setPubSubDomain(true);
        f.setSubscriptionDurable(true);
        f.setSessionTransacted(true);
        f.setConcurrency("1-4");
        f.setMessageConverter(mc);

        f.setErrorHandler(ex -> LoggerFactory.getLogger("JMS-ERROR").error("Listener failed: {}", ex.getMessage(), ex));

        return f;
    }

    @Bean(name = "queueListenerFactory")
    public DefaultJmsListenerContainerFactory queueListenerFactory(ConnectionFactory cf, MessageConverter mc){
        var f = new DefaultJmsListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setSessionTransacted(true);
        f.setConcurrency("2-8");
        f.setMessageConverter(mc);
        f.setErrorHandler(ex -> LoggerFactory.getLogger("JMS-ERROR").error("Queue listener failed: {}", ex.getMessage(), ex));
        return f;
    }
}
