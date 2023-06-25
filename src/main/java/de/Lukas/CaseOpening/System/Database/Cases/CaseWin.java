package de.Lukas.CaseOpening.System.Database.Cases;

import com.google.gson.JsonObject;
import de.Lukas.CaseOpening.System.Database.DatabaseManager;

import java.sql.PreparedStatement;

public class CaseWin {

    public static void createTable() {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("CREATE TABLE IF NOT EXISTS CaseWin (id SERIAL PRIMARY KEY, itemid VARCHAR(50), uuid VARCHAR(50), name VARCHAR(100), material VARCHAR(50), cmd VARCHAR(100))");
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void create(JsonObject jsonObject, String uuid) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("INSERT INTO CaseWin (uuid, itemid, name, material, cmd) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, jsonObject.get("id").getAsString());
            ps.setString(3, jsonObject.get("name").getAsString());
            ps.setString(4, jsonObject.get("item").getAsString());
            ps.setString(5, jsonObject.get("cmd").getAsString());
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
