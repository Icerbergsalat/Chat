package org.example.Server;

import org.example.Client.MessageParser;
import org.example.Server.application.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class TCPThread implements Runnable{
    private Socket socket;
    private org.example.Server.TCPServer server;
    private BufferedReader in;
    private PrintWriter out;

    public TCPThread(Socket socket, org.example.Server.TCPServer server) {
        this.socket = socket;
        this.server = server;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run(){
        boolean loggedIn = false;
        System.out.println("New client connected");
        String message = "Hvad er dit username?";
        TCPServer.knownIps.put("username", socket.getInetAddress() + "");
        try {
            out.println(message);
            while (!loggedIn) {
                String username = in.readLine();
                String[] result = MessageParser.getInstance().unparseMessage(username);
                if (/*UserRepository.getInstance().getUser(result[0]).getUsername().equals(result[0])*/true){
                    loggedIn = true;
                    out.println("logged in succesfully");
                    out.println(result[0]);
                } else {
                    out.println("wrong username");
                    out.println("try again");
                }
            }
            while (true) {
                String communication = in.readLine();
                System.out.println(communication);
                TCPServer.broadcast(communication, this);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } /*catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }
    public void sendMessage(String message){
        out.println(message);
    }
}
