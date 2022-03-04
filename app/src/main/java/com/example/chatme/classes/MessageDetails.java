package com.example.chatme.classes;

public class MessageDetails {
    private String messageText,MessageID,MessageTime,senderID;

    public MessageDetails(String messageText, String messageID, String messageTime,String senderID) {
        this.messageText = messageText;
        this.MessageID = messageID;
        this.MessageTime = messageTime;
        this.senderID=senderID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
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
}
