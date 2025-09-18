package org.example.Client;

import org.example.Client.models.User;

public class MessageParser {
    public String parseMessage(User user, String date, String type, String message) {
        return user.getUsername() + "#" + date + "#" + type + "#" + message;
    }
    public String[] unparseMessage(String message) {
        return message.split("#");
    }
}
