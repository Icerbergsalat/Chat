package org.example.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class TCPThread implements Runnable{
    private Socket socket;
    private org.example.Server.TCPServer server;
    public TCPThread(Socket socket, org.example.Server.TCPServer server) {
        this.socket = socket;
        this.server = server;
    }
    @Override
    public void run(){
        System.out.println("New client connected");
        String message = "Velkommen";
        TCPServer.knownIps.put("username", socket.getInetAddress() + "");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);

            while (true) {
                String communication = in.readLine();
                System.out.println(communication);
                out.println(communication);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
