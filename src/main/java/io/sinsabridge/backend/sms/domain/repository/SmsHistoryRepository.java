// SmsHistoryRepository.java
package io.sinsabridge.backend.sms.domain.repository;

import io.sinsabridge.backend.sms.domain.entity.SmsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmsHistoryRepository extends JpaRepository<SmsHistory, Long> {
    List<SmsHistory> findAllByPhoneNumber(String phoneNumber);

    Optional<SmsHistory> findByPhoneNumberAndVerificationCode(String phoneNumber, String verificationCode);
}
