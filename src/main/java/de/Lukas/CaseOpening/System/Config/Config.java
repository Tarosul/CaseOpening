package de.Lukas.CaseOpening.System.Config;

import de.Lukas.CaseOpening.CaseOpening;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    public static File configFile = new File(CaseOpening.caseOpening.getDataFolder(), "config.yml");
    public static YamlConfiguration configCfg = YamlConfiguration.loadConfiguration(configFile);
    public static void loadConfigFile() {
        configCfg.addDefault("PORT", "5432");
        configCfg.addDefault("DATABASE", "NitCraft");
        configCfg.addDefault("USER", "postgres");
        configCfg.addDefault("PASSWORD", "admin");
        configCfg.addDefault("HOST", "localhost");

        configCfg.options().copyDefaults(true);
        try {
            configCfg.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
