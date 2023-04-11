package io.sinsabridge.backend.domain.entity;

import java.time.LocalDateTime;

public class User {

    private String phoneNumber; // 휴대폰 번호
    private String gender; // 성별
    private int age; // 나이
    private String hobby; // 취미
    private String location; // 사는 곳
    private boolean isPaidUser; // 유료 사용 여부
    private LocalDateTime paymentTime; // 결제 시간
    private LocalDateTime expirationTime; // 만료 기간
    private String profileImageUrl; // 프로필 사진 URL
    private String region; // 사는 지역
    private boolean isPhoneVerified; // 휴대폰 인증 성공 여부
    private LocalDateTime lastPhoneVerificationTime; // 휴대폰 인증 마지막 성공 시간

    public User() {
    }

    // 생성자, getter, setter 및 기타 메서드를 여기에 추가합니다.


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getAge() != user.getAge()) return false;
        if (isPaidUser() != user.isPaidUser()) return false;
        if (isPhoneVerified() != user.isPhoneVerified()) return false;
        if (getPhoneNumber() != null ? !getPhoneNumber().equals(user.getPhoneNumber()) : user.getPhoneNumber() != null)
            return false;
        if (getGender() != null ? !getGender().equals(user.getGender()) : user.getGender() != null) return false;
        if (getHobby() != null ? !getHobby().equals(user.getHobby()) : user.getHobby() != null) return false;
        if (getLocation() != null ? !getLocation().equals(user.getLocation()) : user.getLocation() != null)
            return false;
        if (getPaymentTime() != null ? !getPaymentTime().equals(user.getPaymentTime()) : user.getPaymentTime() != null)
            return false;
        if (getExpirationTime() != null ? !getExpirationTime().equals(user.getExpirationTime()) : user.getExpirationTime() != null)
            return false;
        if (getProfileImageUrl() != null ? !getProfileImageUrl().equals(user.getProfileImageUrl()) : user.getProfileImageUrl() != null)
            return false;
        if (getRegion() != null ? !getRegion().equals(user.getRegion()) : user.getRegion() != null) return false;
        return getLastPhoneVerificationTime() != null ? getLastPhoneVerificationTime().equals(user.getLastPhoneVerificationTime()) : user.getLastPhoneVerificationTime() == null;
    }

    @Override
    public int hashCode() {
        int result = getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0;
        result = 31 * result + (getGender() != null ? getGender().hashCode() : 0);
        result = 31 * result + getAge();
        result = 31 * result + (getHobby() != null ? getHobby().hashCode() : 0);
        result = 31 * result + (getLocation() != null ? getLocation().hashCode() : 0);
        result = 31 * result + (isPaidUser() ? 1 : 0);
        result = 31 * result + (getPaymentTime() != null ? getPaymentTime().hashCode() : 0);
        result = 31 * result + (getExpirationTime() != null ? getExpirationTime().hashCode() : 0);
        result = 31 * result + (getProfileImageUrl() != null ? getProfileImageUrl().hashCode() : 0);
        result = 31 * result + (getRegion() != null ? getRegion().hashCode() : 0);
        result = 31 * result + (isPhoneVerified() ? 1 : 0);
        result = 31 * result + (getLastPhoneVerificationTime() != null ? getLastPhoneVerificationTime().hashCode() : 0);
        return result;
    }
}
