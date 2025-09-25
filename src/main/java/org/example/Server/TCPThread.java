package org.example.Server;

import org.example.Client.MessageParser;
import org.example.Server.application.UserRepository;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class TCPThread implements Runnable{
    private Socket socket;
    private org.example.Server.TCPServer server;
    private BufferedReader in;
    private PrintWriter out;
    public String user;

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
        try {
            out.println(message);
            while (!loggedIn) {
                String username = in.readLine();
                String[] result = MessageParser.getInstance().unparseMessage(username);
                if (/*UserRepository.getInstance().getUser(result[0]).getUsername().equals(result[0])*/true){
                    loggedIn = true;
                    out.println("logged in succesfully");
                    out.println(result[0]);
                    user = result[0];
                } else {
                    out.println("wrong username");
                    out.println("try again");
                }
            }
            while (true) {
                String communication = in.readLine();
                System.out.println(communication);
                if (communication.contains("file")) {
                    MessageParser msgParser = new MessageParser();
                    String[] msg = msgParser.unparseMessage(communication);
                    System.out.println(msg[3]);
                    byte[] fileDataArray = socket.getInputStream().readNBytes(Integer.parseInt(msg[3]));
                    socket.getOutputStream().write(fileDataArray);
                    TCPServer.unicast(fileDataArray, msg[4]);
                    continue;
                }
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
    public void sendMessage(byte[] message) throws IOException {
        socket.getOutputStream().write(message);
    }
}
