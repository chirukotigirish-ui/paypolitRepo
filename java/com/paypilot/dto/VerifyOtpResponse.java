package com.paypilot.dto;

public class VerifyOtpResponse {

    private String username;
    private String purpose;
    private String message;
    private boolean verified;

    public VerifyOtpResponse() {
    }

    public VerifyOtpResponse(
            String username,
            String purpose,
            String message,
            boolean verified) {

        this.username = username;
        this.purpose = purpose;
        this.message = message;
        this.verified = verified;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}