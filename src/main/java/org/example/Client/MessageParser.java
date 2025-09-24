package org.example.Client;

import org.example.Client.models.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageParser {
    private static MessageParser instance;
    public static MessageParser getInstance () {
        if (instance == null){
            instance = new MessageParser();
        }
        return instance;
    }
    public String[] unparseMessage(String message) {
        return message.split("#");
    }

    public String messageBuilder(String type, String user, String payload){
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        String formattedDate = date.format(formatter);
        switch (type) {
            case "message":
                return user + "#" + formattedDate + "#" + type + "#" + payload;
            case "login":
                return user + "#" + formattedDate + "#" + type;
            case "file":
                String[] result = payload.split(":");
                return user + "#" + formattedDate + "#" + type + "#" + result[0] + "#" + result[1];
            default:
                break;
        }
        return type;
    }
}
