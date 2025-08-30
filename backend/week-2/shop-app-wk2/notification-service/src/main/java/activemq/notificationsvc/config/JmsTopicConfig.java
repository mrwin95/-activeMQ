package activemq.notificationsvc.config;

import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsTopicConfig {
    public static final String NOTIFICATIONS_TOPIC = "notifications.topic";

    @Bean
    public DefaultJmsListenerContainerFactory topicJmsListenerContainerFactory(@Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory, @Value("${app.jms.client_id}") String clientId) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true); // topic mode
        factory.setSubscriptionDurable(true); //durable
        factory.setClientId(clientId);
        factory.setSessionTransacted(true); // local JMS tx per message
        factory.setConcurrency("1-2"); // 1 to 3 consumers (scale concurrency)
        factory.setMessageConverter(jacksonJmsMessageConverter());

        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter(){
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTargetType(MessageType.TEXT);
        messageConverter.setTypeIdPropertyName("_type");
        return messageConverter;
    }
}
