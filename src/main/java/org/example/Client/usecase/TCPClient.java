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
        MessageParser messageParser = new MessageParser();


        try (Socket socket = new Socket(host, port)){
            PrintWriter print = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!ThreadIn.loggedin) {
                System.out.println(br.readLine());
                String builder = new MessageParser().messageBuilder("login", scan.nextLine(), null);
                print.println(builder);
                String login = br.readLine();
                System.out.println(login);
                if (login.equals("logged in succesfully"))
                    ThreadIn.loggedin = true;
            }
            String username = br.readLine();

            Thread thread = new Thread(new ThreadIn(socket, br));
            thread.start();

            while(true){
                String msg = scan.nextLine();
                if (msg.contains("/help")) {
                    continue;
                }
                if (msg.contains("/file")) {
                    String[] command = msg.split(" ");
                    //syntax skal være /file "sti" "modtager"
                    try (FileInputStream fis = new FileInputStream(command[1])) {
                        byte[] data = fis.readAllBytes();
                        print.println(messageParser.messageBuilder("file", username,data.length + ":" + command[2]));
                        socket.getOutputStream().write(data);
                        socket.getOutputStream().flush();
                    }
                    continue;
                }

                if (msg.contains("/whisper")){
                    messageParser.messageBuilder("message", "bob", "amogus" + ":" + "bob2");
                }
                String message = messageParser.messageBuilder("message", username, msg);
                String[] result = messageParser.unparseMessage(message);
                System.out.println(result[0] + " | " + result[1] + "\n" + result[3]);
                print.println(message);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void fileSender(String path, String target) {
        // src/main/java/org/example/Client/MessageParser.java
        FileInputStream fis = null;
    }
}