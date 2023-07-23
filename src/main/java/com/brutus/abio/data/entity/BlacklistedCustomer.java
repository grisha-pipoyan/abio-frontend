package com.brutus.abio.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;

@Entity
public class BlacklistedCustomer extends AbstractEntity {

    @Email
    private String email;
    private String phoneNumber;
    private String reason;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

}
