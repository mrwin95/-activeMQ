package activemq.ordersvc.rest;

import activemq.dto.TaskMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    public static final String TASK_QUEUE = "tasks.queue";

    private final JmsTemplate jmsTemplate;


    public TaskController(@Qualifier("queueJmsTemplate") JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping
    public String create(@RequestParam String description, @RequestParam(defaultValue = "LOW") String priority) {
        TaskMessage messageTask = new TaskMessage(UUID.randomUUID().toString(), description);
        MessagePostProcessor processor = message -> {
            message.setStringProperty("priority", priority.toUpperCase());
            return message;
        };

        jmsTemplate.convertAndSend(TASK_QUEUE, messageTask, processor);
        return "queued:" + messageTask.id() + " priority=" + priority;
    }
}
