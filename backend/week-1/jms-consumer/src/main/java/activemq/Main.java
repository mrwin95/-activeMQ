package activemq;

import jakarta.jms.*;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616", "admin", "admin")) {
            String msg;
            try (JMSContext ctx = cf.createContext(Session.AUTO_ACKNOWLEDGE)) {
                Queue queue = ctx.createQueue("demo.queue");
                msg = ctx.createConsumer(queue).receiveBody(String.class, 5000);
                System.out.println("Message received from the queue: " + msg);
            }
        }
    }
}