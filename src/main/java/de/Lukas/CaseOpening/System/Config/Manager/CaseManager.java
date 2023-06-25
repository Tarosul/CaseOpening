package de.Lukas.CaseOpening.System.Config.Manager;

import com.google.gson.*;
import de.Lukas.CaseOpening.CaseOpening;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class CaseManager {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void addItem(String caseType, String itemStack, String name, String lore, String slot, String price) {
        try {
            JsonObject item = getCase();
            JsonArray array = getItems();
            JsonObject object = new JsonObject();

            object.addProperty("id", array.size() + 1);
            object.addProperty("case_type", caseType);
            object.addProperty("item", itemStack);
            object.addProperty("name", name);
            object.addProperty("lore", lore);
            object.addProperty("slot", slot);
            object.addProperty("price", price);

            array.add(object);

            if (item.get("items") != null) {
                item.remove("items");
            }
            item.add("items", array);

            saveItem(item);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveItem(JsonElement element) {
        try {
            File tempFolder = new File(CaseOpening.caseOpening.getDataFolder().getAbsolutePath() + "/");
            tempFolder.mkdirs();

            File file = new File(tempFolder.getAbsolutePath(), "case.yml");

            try (Writer writer = new FileWriter(file)) {
                gson.toJson(element, writer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JsonObject getCase() {
        File tempFolder = new File(CaseOpening.caseOpening.getDataFolder().getAbsolutePath() + "/");
        tempFolder.mkdirs();

        File file = new File(tempFolder.getAbsolutePath(), "case.yml");
        return ConfigManager.getJsonObject(file);
    }

    public static JsonArray getItems() {
        JsonObject caseObject = getCase();

        JsonArray array;

        try {
            array = caseObject.get("items").getAsJsonArray();
            if (array == null) {
                array = new JsonArray();
            }
        } catch (Exception ex) {
            array = new JsonArray();
        }

        return array;
    }
}
