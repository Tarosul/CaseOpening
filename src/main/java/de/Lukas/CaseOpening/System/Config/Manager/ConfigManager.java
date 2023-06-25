package de.Lukas.CaseOpening.System.Config.Manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import de.Lukas.CaseOpening.System.Config.Config;
import de.Lukas.CaseOpening.System.Config.ConfigType;

import java.io.File;
import java.io.FileReader;

public class ConfigManager {

    private String path;
    private ConfigType configType;

    public ConfigManager(ConfigType configType, String path) {
        this.path = path;
        this.configType = configType;
    }

    public static String getValue(ConfigType type, String path) {
        return switch (type) {
            case CONFIG -> Config.configCfg.getString(path);
        };
    }

    public int intValue() {
        int i = 0;
        try {
            i = Integer.valueOf(getValue(this.configType, this.path));
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }
        return i;
    }

    public String stringValue() {
        return getValue(this.configType, this.path);
    }

    static JsonObject getJsonObject(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject object;

        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            object = gson.fromJson(reader, JsonObject.class);
            reader.close();
        } catch (Exception ex) {
            object = new JsonObject();
        }

        return object;
    }
}

