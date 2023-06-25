package de.Lukas.CaseOpening.System.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private String host;
    private String port;
    public static String database;
    private String user;
    private String password;
    private static Connection con;
    boolean connected = false;

    public static boolean isConnected() {
        return con != null;
    }

    public DatabaseManager(String host, String port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;

        connect();
    }

    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.user, this.password);
            this.connected = true;
        } catch (SQLException e) {
            this.connected = false;
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