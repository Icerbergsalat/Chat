package org.example.Server;

import java.io.IOException;
import java.net.ServerSocket;
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
    static HashMap<String, String> knownIps = new HashMap<>();

    public TCPServer(int port, int pool) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.pool = Executors.newFixedThreadPool(pool);
    }

    @Override
    public void run() {
        try {
            while (true) {
                pool.execute(new TCPThread(serverSocket.accept(), this));
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
}