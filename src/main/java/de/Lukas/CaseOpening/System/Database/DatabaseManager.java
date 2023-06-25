package de.Lukas.CaseOpening.System.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private String HOST;
    private String PORT;
    public static String DATABASE;
    private String USER;
    private String PASSWORD;
    private static Connection con;
    boolean Connected = false;

    public static boolean isConnected() {
        return con != null;
    }

    public DatabaseManager(String host, String port, String database, String user, String password) {
        this.HOST = host;
        this.PORT = port;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;

        connect();
    }

    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:postgresql://" + this.HOST + ":" + this.PORT + "/" + this.DATABASE + "?autoReconnect=true", this.USER, this.PASSWORD);
            this.Connected = true;
        } catch (SQLException e) {
            this.Connected = false;
            System.out.println("Error!");
            System.out.println(e);
        }
    }

    public static void disconnect() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PreparedStatement getStatement(String sql) {
        if (isConnected()) {
            try {
                return con.prepareStatement(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}