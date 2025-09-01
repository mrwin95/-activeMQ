package activemq.ordersvc.repo;

import activemq.ordersvc.domain.OutboxEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
        SELECT * FROM outbox
        WHERE status='PENDING'
        ORDER BY id ASC
        LIMIT :limit
        FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    List<OutboxEntity> fetchBatchForPublish(int limit);
}
