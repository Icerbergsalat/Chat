package org.example.Client;

public class MessageParser {
    public String parseMessage(User user, String date, String type, String message) {
        return user.getUsername() + "#" + date + "#" + type + "#" + message;
    }
    public String[] unparseMessage(String message) {
        return message.split("#");
    }
}
