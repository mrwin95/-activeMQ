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
        // connection factory with durable clientid
        var cf = new ActiveMQConnectionFactory(url, user, pass);
        cf.setInitialConnectAttempts(20);
        cf.setRetryInterval(1000);
        cf.setRetryIntervalMultiplier(2.0);
        cf.setReconnectAttempts(-1);
        return cf;
    }

    @Bean(name = "jmsConnectionFactory")
    @Primary
    public ConnectionFactory connectionFactory(@Qualifier("artemisTargetCf") ActiveMQConnectionFactory target,
                                               @Value("${app.jms.client_id}") String clientId) {
        var cf = new SingleConnectionFactory(target);
        cf.setClientId(clientId); // required for durable
        cf.setReconnectOnException(true);
        return cf;
    }
}
