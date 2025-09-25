package org.example.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadIn implements Runnable {
    private Socket socket;
    private BufferedReader bufferedReader;
    public static boolean loggedin = false;
    org.example.Client.MessageParser messageParser = new org.example.Client.MessageParser();

    public ThreadIn(Socket socket, BufferedReader bufferedReader) {
        this.socket = socket;
        this.bufferedReader = bufferedReader;
    }
    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(true) {
                String[] result = messageParser.unparseMessage(br.readLine());
                    //System.out.println(result[0] + " | " + result[1] + "\n" + result[3]);

                    //name | tid
                    //-> text
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
