package activemq.ordersvc.service;

import activemq.dto.CreateOrderRequest;
import activemq.dto.OrderCreatedMessage;
import activemq.ordersvc.config.JmsConfig;
import activemq.ordersvc.config.TopicConfig;
import activemq.ordersvc.domain.OrderEntity;
//import activemq.ordersvc.messaging.OrderCreatedMessage;
import activemq.ordersvc.repo.OrderRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Service
public class OrderService {

    private final OrderRespository orderRespository;

    private final JmsTemplate jmsTemplate;

    private final JmsTemplate topicJmsTemplate;

    private final ObjectMapper objectMapper;

    private final JdbcTemplate jdbcTemplate;

    public OrderService(OrderRespository orderRespository, @Qualifier("queueJmsTemplate") JmsTemplate jmsTemplate, @Qualifier("topicJmsTemplate") JmsTemplate topicJmsTemplate, ObjectMapper objectMapper, JdbcTemplate jdbcTemplate) {
        this.orderRespository = orderRespository;
        this.jmsTemplate = jmsTemplate;
        this.topicJmsTemplate = topicJmsTemplate;
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public OrderEntity create(String customerName, BigDecimal total) {

        // Persist order

        OrderEntity orderEntity =new OrderEntity();
        orderEntity.setCustomerName(customerName);
        orderEntity.setTotal(total);
        orderEntity.setCreatedAt(OffsetDateTime.now());
        orderRespository.save(orderEntity);

        // Publish event JMS locally session

        OrderCreatedMessage orderCreatedMessage = new activemq.dto.OrderCreatedMessage(orderEntity.getOrderId(), orderEntity.getCustomerName(), orderEntity.getTotal());
        jmsTemplate.convertAndSend(JmsConfig.ORDER_CREATED_QUEUE, orderCreatedMessage);

        // publish to topic

        topicJmsTemplate.convertAndSend(TopicConfig.NOTIFICATIONS_TOPIC, orderCreatedMessage);
        return orderEntity;
    }

    @Transactional
    public OrderEntity createOrder(CreateOrderRequest req) throws JsonProcessingException {
        var orderEntity = new OrderEntity();
        orderEntity.setTotal(req.total());
        orderEntity.setCreatedAt(OffsetDateTime.now());
        orderEntity.setCustomerName(req.customerName());
        var order = orderRespository.save(orderEntity);
        var msg = new OrderCreatedMessage(order.getOrderId(), order.getCustomerName(), order.getTotal());
        String eventKey = "order:" + order.getOrderId() + ":v1";
        String json = objectMapper.writeValueAsString(msg);

    jdbcTemplate.update("""
        insert into outbox(aggregate_type, aggregate_id,payload,event_key) 
        values('Order', ?, 'OrderCreated', ?::jsonb, ?)
        """, order.getOrderId(), json,eventKey);
        return order;
    }
}
