package activemq.notificationsvc.repo;

import activemq.notificationsvc.domain.IdempotencyGuard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyGuardRepository extends JpaRepository<IdempotencyGuard, String> {
}
