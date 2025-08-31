package activemq.notificationsvc.config;

import activemq.dto.OrderCreatedMessage;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
//import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.core.metrics.Metric;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumeMetrics {

    private final Counter ok = Counter.builder("jms_consume_ok")
            .description("Number of successful JMS message consumptions")
            .register(Metrics.globalRegistry);
    private final Counter fail = Counter.builder("jms_consume_fail")
            .description("Number of failed JMS message consumptions")
            .register(Metrics.globalRegistry);

    @JmsListener(destination = JmsTopicConfig.NOTIFICATIONS_TOPIC,
        subscription = "${app.jms.shared.subscription_name}",
        containerFactory = "sharedListenerFactory")
    public void onMessage(OrderCreatedMessage msg){
        try {
            ok.increment();
        }catch (RuntimeException e){
            fail.increment();
            throw e;
        }
    }
}
