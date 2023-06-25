package de.Lukas.CaseOpening.System.Database.Credits;

import de.Lukas.CaseOpening.System.Database.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Credits {

    public static void createTable() {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("CREATE TABLE IF NOT EXISTS CaseCredits (uuid VARCHAR(100), amount INT)");
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void register(String uuid, int amount) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("INSERT INTO CaseCredits (uuid, amount) VALUES (?, ?)");
            ps.setString(1, uuid);
            ps.setInt(2, amount);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isRegistered(String uuid) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("SELECT * FROM CaseCredits WHERE uuid = ?");
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

    public static int getCredits(String uuid) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("SELECT * FROM CaseCredits WHERE uuid = ?");
            ps.setString(1, uuid);
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

    public static void setCredits(int amount, String uuid) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("UPDATE CaseCredits SET amount = ? WHERE uuid = ?");
            ps.setInt(1, amount);
            ps.setString(2, uuid);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
