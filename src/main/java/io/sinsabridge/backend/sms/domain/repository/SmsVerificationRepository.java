package io.sinsabridge.backend.sms.domain.repository;

import io.sinsabridge.backend.sms.domain.entity.SmsVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsVerificationRepository extends JpaRepository<SmsVerification, Long> {

    /**
     * 전화번호로 SmsVerification 엔티티를 조회합니다.
     *
     * @param phoneNumber 전화번호
     * @return 조회된 SmsVerification 엔티티
     */
    SmsVerification findByPhoneNumberAndVerificationCode(String phoneNumber, String verificationCode);


}
