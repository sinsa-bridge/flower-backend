// SmsHistoryRepository.java
package io.sinsabridge.backend.domain.repository;

import io.sinsabridge.backend.domain.entity.SmsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsHistoryRepository extends JpaRepository<SmsHistory, Long> {
}
