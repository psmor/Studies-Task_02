package psmor.limit.repositoriy;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import psmor.limit.entity.Limits;

import java.util.Optional;

@Repository
public interface LimitsRepository extends JpaRepository<Limits, Long> {
        Optional<Limits> findByUserId(Integer userId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE change_limits SET change_limit = :balance", nativeQuery = true)
        void updateChangeLimitsAll(@Param("balance") double balance);
}
