package org.example.Server.application;

import org.example.Client.models.User;
import org.example.Server.application.DBConnector;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private DBConnector dbConnector;
    String url = "jdbc:sqlite:chatdb.db";

    public UserRepository(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }
    public void save(User user) throws SQLException {
        String sql = "insert into user (ID, username) values (?,?)";

            var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.executeUpdate();
    }
    public void findAll() throws SQLException {
        String sql = "select * from user";
        var conn = DriverManager.getConnection(url);
        var stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("ID");
            String username = rs.getString("username");
            System.out.println(id + " " + username);
        }
    }
    public void findByUsername(String username) throws SQLException {
        String sql = "select * from user where username = ?";
        var conn = DriverManager.getConnection(url);
        var stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
        }
    }
    public void update(User user) throws SQLException {
        String sql = "update user set username = ? where ID = ?";
        var conn = DriverManager.getConnection(url);
        var stmt = conn.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setInt(2, user.getId());
        stmt.executeUpdate();
    }
    public void delete(int id) throws SQLException {
        String sql = "delete from user where ID = ?";
        var conn = DriverManager.getConnection(url);
        var stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
