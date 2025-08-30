package activemq.ordersvc.messaging;

import activemq.dto.TaskMessage;
import activemq.ordersvc.rest.TaskController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class HighPriorityListener {
    private static final Logger log = LoggerFactory.getLogger(HighPriorityListener.class);

    @JmsListener(destination =  TaskController.TASK_QUEUE, selector = "priority = 'HIGH'", containerFactory ="jmsListenerContainerFactory")
    public void handle(TaskMessage taskMessage) {
        log.info("[HIGH] processing task id={} desc={}", taskMessage.id(), taskMessage.description());
    }
}
