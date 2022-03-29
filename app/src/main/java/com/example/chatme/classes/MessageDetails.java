package com.example.chatme.classes;

public class MessageDetails {
    private String messageText,MessageID,MessageTime,senderID,messageType,isSeen;

    public MessageDetails(String messageText, String messageID, String messageTime, String senderID, String messageType, String isSeen) {
        this.messageText = messageText;
        MessageID = messageID;
        MessageTime = messageTime;
        this.senderID = senderID;
        this.messageType = messageType;
        this.isSeen = isSeen;
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

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }
}
