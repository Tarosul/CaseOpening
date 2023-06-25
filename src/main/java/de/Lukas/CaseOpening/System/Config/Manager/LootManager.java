package de.Lukas.CaseOpening.System.Config.Manager;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

import de.Lukas.CaseOpening.CaseOpening;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LootManager {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static void addItem(String caseType, String itemStack, String name, String rare, String command) {
        try {
            JsonObject item = getCaseLoot(caseType);
            JsonArray array = getCaseLootItems(caseType);
            JsonObject object = new JsonObject();

            object.addProperty("id", array.size() + 1);
            object.addProperty("item", itemStack);
            object.addProperty("name", name);
            object.addProperty("cmd", command);
            object.addProperty("rare", rare);
            array.add(object);

            if (item.get("items") != null) {
                item.remove("items");
            }
            item.add("items", array);

            saveItem(caseType, item);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveItem(String s, JsonElement element) {
        try {
            File tempFolder = new File(CaseOpening.caseOpening.getDataFolder().getAbsolutePath() + "/" + "cases" + "/");
            tempFolder.mkdirs();

            File file = new File(tempFolder.getAbsolutePath(), s + ".yml");

            try (Writer writer = new FileWriter(file)) {
                gson.toJson(element, writer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JsonObject getCaseLoot(String casetype) {
        File tempFolder = new File(CaseOpening.caseOpening.getDataFolder().getAbsolutePath() + "/" + "cases" + "/");
        tempFolder.mkdirs();

        File file = new File(tempFolder.getAbsolutePath(), casetype + ".yml");
        return ConfigManager.getJsonObject(file);
    }

    public static ArrayList<String> getCaseLootTypes() {
        File tempFolder = new File(CaseOpening.caseOpening.getDataFolder().getAbsolutePath() + "/" + "cases" + "/");
        tempFolder.mkdirs();
        ArrayList<String> type = new ArrayList<>();

        for (File file : tempFolder.listFiles()) {
            type.add(FilenameUtils.getBaseName(file.getName()));
        }
        return type;
    }

    public static JsonArray getCaseLootItems(String caseType) {
        JsonObject loot = getCaseLoot(caseType);

        JsonArray array;

        try {
            array = loot.get("items").getAsJsonArray();
            if (array == null) {
                array = new JsonArray();
            }
        } catch (Exception ex) {
            array = new JsonArray();
        }

        return array;
    }

    public static JsonObject getLoot(String type) {
        JsonArray lootList = getCaseLootItems(type);
        int random = new Random().nextInt(100);
        JsonObject loot = lootList.get(new Random().nextInt(lootList.size())).getAsJsonObject();

        while(random > loot.get("rare").getAsInt()) {
            random = new Random().nextInt(100);
            loot = lootList.get(new Random().nextInt(lootList.size())).getAsJsonObject();
        }

        return loot;
    }
}
