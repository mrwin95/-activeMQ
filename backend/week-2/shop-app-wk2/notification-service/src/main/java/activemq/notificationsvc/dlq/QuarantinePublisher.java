package activemq.notificationsvc.dlq;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class QuarantinePublisher {

    public static final String QUARANTINE_QUEUE = "quarantine.queue";
    private final JmsTemplate queueJmsTemplate;


    public QuarantinePublisher(JmsTemplate queueJmsTemplate) {
        this.queueJmsTemplate = queueJmsTemplate;
    }

    public void park(Object payload){
        queueJmsTemplate.convertAndSend(QUARANTINE_QUEUE, payload);
    }
}
