package psmor.limit.repositoriy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psmor.limit.entity.ChangeLimits;
import psmor.limit.entity.Limits;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChangeLimitsRepository extends JpaRepository<ChangeLimits, Long> {
    Optional<ChangeLimits> findByTransactionId(UUID transactionId);
    List<ChangeLimits> findByLimId(Long limId);
}
