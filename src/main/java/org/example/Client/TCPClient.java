package org.example.Client;

import org.example.Client.models.User;

import java.io.*;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.LocalDateTime;

public class TCPClient{
    public static void main(String[] args){
        String host = "localhost";
        int port = 6969;
        Scanner scan = new Scanner(System.in);
        User user = new User(1, "bob");
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        String formattedDate = date.format(formatter);
        String file = "Text";
        org.example.Client.MessageParser messageParser = new org.example.Client.MessageParser();


        try (Socket socket = new Socket(host, port)){
            PrintWriter print = new PrintWriter(socket.getOutputStream(), true);

            Thread thread = new Thread(new ThreadIn(socket));
            thread.start();

            while(true){
                String message = messageParser.parseMessage(user, formattedDate, file, scan.nextLine());
                print.println(message);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}