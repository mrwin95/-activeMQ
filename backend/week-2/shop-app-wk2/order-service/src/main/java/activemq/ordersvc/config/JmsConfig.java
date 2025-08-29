package activemq.ordersvc.config;

import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@Configuration
@EnableJms
public class JmsConfig {

    public static final String ORDER_CREATED_QUEUE = "order.created.queue";

    @Bean
    public DefaultJmsListenerContainerFactory createFactory(@Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSessionTransacted(true); // local JMS tx per message
        factory.setConcurrency("1-3"); // 1 to 3 consumers (scale concurrency)
        factory.setMessageConverter(jacksonJmsMessageConverter());
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(@Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDeliveryPersistent(true);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        return jmsTemplate;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter(){
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTargetType(MessageType.TEXT);
        messageConverter.setTypeIdPropertyName("_type");
        return messageConverter;
    }
}
