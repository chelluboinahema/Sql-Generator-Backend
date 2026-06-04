package com.hema.sqlbot.modal.Ai;



public class Message {

    private String role;
    private String content;

    public Message(String content, String role) {
        this.content = content;
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}