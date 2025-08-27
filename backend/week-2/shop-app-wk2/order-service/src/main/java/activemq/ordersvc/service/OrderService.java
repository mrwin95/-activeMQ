package activemq.ordersvc.service;

import activemq.ordersvc.domain.OrderEntity;
import activemq.ordersvc.messaging.OrderCreatedMessage;
import activemq.ordersvc.repo.OrderRespository;
import jakarta.transaction.Transactional;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class OrderService {

    private final OrderRespository orderRespository;
    private final JmsTemplate jmsTemplate;


    public OrderService(OrderRespository orderRespository, JmsTemplate jmsTemplate) {
        this.orderRespository = orderRespository;
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    public OrderEntity create(String customerName, BigDecimal total) {

        // Persist order

        OrderEntity orderEntity = OrderEntity.builder()
                .customerName(customerName)
                .total(total)
                .createdAt(OffsetDateTime.now())
                .build();

        orderRespository.save(orderEntity);

        // Publish event JMS locally session

        OrderCreatedMessage orderCreatedMessage = new OrderCreatedMessage(orderEntity.getOrderId(), orderEntity.getCustomerName(), orderEntity.getTotal());
        jmsTemplate.convertAndSend(orderCreatedMessage);
        return orderEntity;
    }
}
