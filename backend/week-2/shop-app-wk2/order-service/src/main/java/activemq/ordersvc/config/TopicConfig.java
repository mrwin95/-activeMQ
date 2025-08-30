package activemq.ordersvc.config;

import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class TopicConfig {

    public static final String NOTIFICATIONS_TOPIC = "notifications.topic";

    @Bean(name = "topicJmsTemplate")
    public JmsTemplate topicJmsTemplate(@Qualifier("jmsConnectionFactory") ConnectionFactory cf) {
        JmsTemplate t = new JmsTemplate(cf);
        t.setPubSubDomain(true);     // <-- Topic mode
        t.setSessionTransacted(true);
        t.setDeliveryPersistent(true);
        t.setMessageConverter(jacksonJmsMessageConverter());
        return t;
    }

    @Bean(name = "topicJacksonJmsMessageConverter")
    public MessageConverter jacksonJmsMessageConverter(){
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTargetType(MessageType.TEXT);
        messageConverter.setTypeIdPropertyName("_type");
        return messageConverter;
    }
}
