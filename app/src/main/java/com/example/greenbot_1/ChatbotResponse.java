package com.example.greenbot_1;

public class ChatbotResponse {
    private String userId; // The ID of the user or chatbot who sent the message
    private String userName; // The name of the user or chatbot
    private String message; // The text message content
    private String timestamp; // The timestamp when the message was sent

    public ChatbotResponse() {
        // Default constructor required for Firestore
    }

    public ChatbotResponse(String userId, String userName, String message, String timestamp) {
        this.userId = userId;
        this.userName = userName;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
