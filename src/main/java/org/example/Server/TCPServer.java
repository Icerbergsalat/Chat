package org.example.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.sql.*;

import org.example.Client.models.User;
import org.example.Server.application.DBConnector;

public class TCPServer implements Runnable {
    public static TCPServer tcpServer;
    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    static ArrayList<TCPThread> users = new ArrayList<>();

    public TCPServer(int port, int pool) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.pool = Executors.newFixedThreadPool(pool);
    }

    @Override
    public void run() {
        try {
            while (true) {
                TCPThread thread = new TCPThread(serverSocket.accept(), this);
                users.add(thread);
                pool.execute(thread);
            }
        } catch (IOException e) {
            pool.shutdown();
        }
    }

    public static void main(String[] args) {
        try {
            Thread thread = new Thread(new TCPServer(6969, 10));
            thread.start();
            System.out.println("Server started");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        DBConnector db = new DBConnector();
        Connection connection = db.getConnection();

        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static boolean broadcast(String message, TCPThread sender) {
        for (TCPThread user : users) {
            if (user != sender) {
                user.sendMessage(message);
                return true;
            }
        }
        return false;
    }
    public static boolean unicast(String message, String reciever) {
        TCPThread thread = null;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).user.equalsIgnoreCase(reciever)){
                thread = users.get(i);
                break;
            }
        }
        if (thread == null) {
            return false;
        }
        thread.sendMessage(message);
        return true;
    }
    public static boolean unicast(byte[] file, String reciever) throws IOException {
        TCPThread thread = null;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).user.equalsIgnoreCase(reciever)){
                thread = users.get(i);
                break;
            }
        }
        if (thread == null) {
            return false;
        }
        thread.sendMessage(file);
        return true;
    }
}