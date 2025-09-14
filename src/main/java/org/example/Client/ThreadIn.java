package org.example.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadIn implements Runnable {
    private Socket socket;
    org.example.Client.MessageParser messageParser = new org.example.Client.MessageParser();

    public ThreadIn(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(true) {
                String[] fuck = messageParser.unparseMessage(br.readLine());
                for (String s : fuck){
                    System.out.println(s);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
