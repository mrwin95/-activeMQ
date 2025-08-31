package activemq.ordersvc.outbox;

import activemq.dto.OrderCreatedMessage;
import activemq.ordersvc.messaging.OrderEventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OutboxPublisher {

    private final JdbcTemplate jdbcTemplate;
    private final OrderEventPublisher orderEventPublisher;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(JdbcTemplate jdbcTemplate, OrderEventPublisher orderEventPublisher, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderEventPublisher = orderEventPublisher;
        this.objectMapper = objectMapper;
    }

    public void publish() throws JsonProcessingException {
        var rows = jdbcTemplate.query(
                """
                    select id, payload from outbox
                    where published_at IS null
                    order by id asc
                    limit 100
                """, (rs, n) -> new Object[]{rs.getLong("id"), rs.getString("payload")});

        for(var r : rows) {
            long id = (long)r[0];
            var payload = objectMapper.readValue((String)r[1], OrderCreatedMessage.class);
            orderEventPublisher.publishCreated(payload);
            jdbcTemplate.update("update outbox set published_at = now() where id = ? AND published_at is null", id);

        }
    }
}
