package activemq.notificationsvc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Map;

@Configuration
public class JmsJsonConfig {

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper om) {
        var c = new MappingJackson2MessageConverter();
        c.setTargetType(MessageType.TEXT);
        c.setTypeIdPropertyName("_type");
        c.setTypeIdMappings(Map.of(
                "orderCreated", activemq.dto.OrderCreatedMessage.class,
                "activemq.dto.OrderCreatedMessage", activemq.dto.OrderCreatedMessage.class
        ));

        c.setObjectMapper(om);
        return  c;
    }
}
