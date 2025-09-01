package activemq.notificationsvc.domain;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Table(name = "processed_events")
public class IdempotencyGuard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "event_key")
    private String eventKey;
    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    public IdempotencyGuard(Integer id, String eventKey, OffsetDateTime processedAt) {
        this.id = id;
        this.eventKey = eventKey;
        this.processedAt = processedAt;
    }

    public IdempotencyGuard() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(OffsetDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
