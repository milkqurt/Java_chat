package com.example.chack_chack_messanger.entity;

public class Message {

    private String text;
    private String senderId;
    private String receiverId;

    public Message() {
    }

    public Message(String text, String senderId, String receiverId) {
        this.text = text;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }
}
