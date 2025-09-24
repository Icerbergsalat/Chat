package org.example.Client.usecase;

import org.example.Client.ThreadIn;
import org.example.Client.MessageParser;

import java.io.*;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.LocalDateTime;

public class TCPClient{
    public static void main(String[] args){
        String host = "localhost";
        int port = 6969;
        ThreadIn.loggedin = false;
        Scanner scan = new Scanner(System.in);
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        String formattedDate = date.format(formatter);
        String type = "login";
        MessageParser messageParser = new MessageParser();


        try (Socket socket = new Socket(host, port)){
            PrintWriter print = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!ThreadIn.loggedin) {
                System.out.println(br.readLine());
                String builder = new MessageParser().messageBuilder(type, scan.nextLine(), null);
                print.println(builder);
                String login = br.readLine();
                System.out.println(login);
                if (login.equals("logged in succesfully"))
                    ThreadIn.loggedin = true;
            }

            Thread thread = new Thread(new ThreadIn(socket, br));
            thread.start();

            while(true){
                String message = messageParser.messageBuilder("message", "harry", scan.nextLine());
                print.println(message);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}