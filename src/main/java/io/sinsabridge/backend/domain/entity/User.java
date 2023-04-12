package io.sinsabridge.backend.domain.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class User implements UserDetails {

    // 기존 코드
    private String phoneNumber; // 휴대폰 번호

    private String password; // 비밀번호

    private String gender; // 성별
    private int age; // 나이
    private String hobby; // 취미
    private String location; // 사는 곳

    // 새로 추가된 코드
    private boolean isPaidUser; // 유료 사용 여부
    private LocalDateTime paymentTime; // 결제 시간
    private LocalDateTime expirationTime; // 만료 기간

    // 기존 코드
    private String profileImageUrl; // 프로필 사진 URL
    private String region; // 사는 지역
    private boolean isPhoneVerified; // 휴대폰 인증 성공 여부
    private LocalDateTime lastPhoneVerificationTime; // 휴대폰 인증 마지막 성공 시간

    // 기존 코드 및 새로 추가된 코드에서 사용하는 UserDetails 인터페이스 메서드들
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (isPaidUser && expirationTime.isAfter(LocalDateTime.now())) {
            return Collections.singleton(() -> "ROLE_PAID_USER");
        } else {
            return Collections.singleton(() -> "ROLE_LIMITED_USER");
        }
    }

    @Override
    public String getPassword() {
        //  비밀번호 인증을 사용하지 않으므로 null을 반환합니다.
        return null;
    }

    @Override
    public String getUsername() {
        // 휴대폰 번호를 사용자 이름으로 사용합니다.
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부를 확인합니다.  만료되지 않는 계정만 사용하므로 true를 반환합니다.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 여부를 확인합니다.  잠기지 않은 계정만 사용하므로 true를 반환합니다.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 인증 정보(비밀번호) 만료 여부를 확인합니다.  만료되지 않는 인증 정보만 사용하므로 true를 반환합니다.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 사용자 계정이 활성화되어 있는지 확인합니다.
        //  휴대폰 인증이 완료된 사용자만 활성화된 계정으로 간주하고, 그렇지 않은 경우 비활성화된 계정으로 간주합니다.
        return isPhoneVerified;
    }

    // 기존 코드의 생성자, getter, setter, equals, hashCode, toString 메서드들 생략

    // 새로 추가된 코드의 getter, setter 메서드들
    public boolean isPaidUser() {
        return isPaidUser;
    }

    public void setPaidUser(boolean paidUser) {
        isPaidUser = paidUser;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isPhoneVerified() {
        return isPhoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }

    public LocalDateTime getLastPhoneVerificationTime() {
        return lastPhoneVerificationTime;
    }

    public void setLastPhoneVerificationTime(LocalDateTime lastPhoneVerificationTime) {
        this.lastPhoneVerificationTime = lastPhoneVerificationTime;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
