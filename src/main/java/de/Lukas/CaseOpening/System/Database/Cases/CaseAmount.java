package de.Lukas.CaseOpening.System.Database.Cases;

import de.Lukas.CaseOpening.System.Database.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CaseAmount {

    public static void createTable() {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("CREATE TABLE IF NOT EXISTS CaseAmount (uuid VARCHAR(100), type VARCHAR(50), amount INT)");
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void register(String uuid, String type, int amount) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("INSERT INTO CaseAmount (uuid, type, amount) VALUES (?, ?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, type);
            ps.setInt(3, amount);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isRegistered(String uuid) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("SELECT * FROM CaseAmount WHERE uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            boolean user = rs.next();
            rs.close();
            rs.close();
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static int getCases(String uuid, String type) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("SELECT * FROM CaseAmount WHERE uuid = ? AND type = ?");
            ps.setString(1, uuid);
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int level = rs.getInt("amount");
            rs.close();
            ps.close();
            return level;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static void setCases(String uuid, String type, int amount) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("UPDATE CaseAmount SET amount = ? WHERE uuid = ? AND type = ?");
            ps.setInt(1, amount);
            ps.setString(2, uuid);
            ps.setString(3, type);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
