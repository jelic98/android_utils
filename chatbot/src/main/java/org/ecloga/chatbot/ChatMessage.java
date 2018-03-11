package org.ecloga.chatbot;

public class ChatMessage {

    private String text, user;

    public ChatMessage() {}

    public ChatMessage(String text, String user){
        this.text = text;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}