package activemq.notificationsvc.config;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.SingleConnectionFactory;

@Configuration
public class JmsClientIdConfig {

    @Bean
    public ActiveMQConnectionFactory artemisTargetCf(@Value("${spring.artemis.broker-url}") String url,
                                                     @Value("${spring.artemis.user}") String user,
                                                     @Value("${spring.artemis.password}") String pass) {
        ActiveMQConnectionFactory afc = new ActiveMQConnectionFactory(url, user, pass);
        return  afc;
    }

    @Bean(name = "jmsConnectionFactory")
    @Primary
    public ConnectionFactory connectionFactory(@Qualifier("artemisTargetCf") ActiveMQConnectionFactory target, @Value("${app.jms.client_id}") String clientId) {
        SingleConnectionFactory cf = new SingleConnectionFactory(target);
        cf.setClientId(clientId);
        cf.setReconnectOnException(true);
        return cf;
    }
}
