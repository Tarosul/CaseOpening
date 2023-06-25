package de.Lukas.CaseOpening.System.Database.Cases;

import com.google.gson.JsonObject;
import de.Lukas.CaseOpening.System.Database.DatabaseManager;
import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CaseType {

    public static void createTable() {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("CREATE TABLE IF NOT EXISTS CaseType (id SERIAL PRIMARY KEY, type VARCHAR(50), name VARCHAR(100), material VARCHAR(50))");
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void create(String type, String name, Material material) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("INSERT INTO CaseType (type, name, material) VALUES (?, ?, ?)");
            ps.setString(1, type);
            ps.setString(2, name);
            ps.setString(3, material.name());
            ps.executeUpdate();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isCreated(String type) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("SELECT * FROM CaseType WHERE type = ?");
            ps.setString(1, type);
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

    public static JsonObject getCaseType(String type) {
        try {
            PreparedStatement ps = DatabaseManager.getStatement("SELECT * FROM CaseType WHERE type = ?");
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            rs.next();
            JsonObject object = new JsonObject();
            object.addProperty("id", rs.getInt("id"));
            object.addProperty("name", rs.getString("name"));
            object.addProperty("type", rs.getString("type"));
            object.addProperty("material", rs.getString("material"));
            rs.close();
            ps.close();
            return object;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<JsonObject> getCaseTypes() {
        List<JsonObject> values = new ArrayList<JsonObject>();
        try {
            PreparedStatement ps = DatabaseManager.getStatement("SELECT * FROM CaseType");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                JsonObject object = new JsonObject();
                object.addProperty("id", rs.getString("id"));
                object.addProperty("name", rs.getString("name"));
                object.addProperty("type", rs.getString("type"));
                object.addProperty("material", rs.getString("material"));
                values.add(object);
            }
            rs.close();
            ps.close();
            return values;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
