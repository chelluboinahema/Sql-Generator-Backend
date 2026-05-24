package com.hema.sqlbot.modal.Ai;

public class Choice {

    private Message message;
    public Choice() {
    }
    public Choice(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}