package com.example.chatme.classes;

public class MessageDetails {
    private String messageText,MessageID,MessageTime,senderID,messageType;

    public MessageDetails(String messageText, String messageID, String messageTime, String senderID, String messageType) {
        this.messageText = messageText;
        MessageID = messageID;
        MessageTime = messageTime;
        this.senderID = senderID;
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageID() {
        return MessageID;
    }

    public void setMessageID(String messageID) {
        MessageID = messageID;
    }

    public String getMessageTime() {
        return MessageTime;
    }

    public void setMessageTime(String messageTime) {
        MessageTime = messageTime;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
