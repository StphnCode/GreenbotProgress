package com.example.greenbot_1;

public class Conversation {
    private String sender; // "User"
    private String userId;
    private String userName;
    private String message;
    private String timestamp;
    private String uniqueMessageId;

    public Conversation() {
        // Default constructor required for Firestore
    }

    public Conversation(String sender, String userId, String userName, String message, String timestamp, String uniqueMessageId) {
        this.sender = sender;
        this.userId = userId;
        this.userName = userName;
        this.message = message;
        this.timestamp = timestamp;
        this.uniqueMessageId = uniqueMessageId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getUniqueMessageId(){
        return uniqueMessageId;
    }

    public void setMessageId(String uniqueMessageId){
        this.uniqueMessageId = uniqueMessageId;
    }
}
