package de.Lukas.CaseOpening;

import de.Lukas.CaseOpening.Command.Item;
import de.Lukas.CaseOpening.Command.Spawn;
import de.Lukas.CaseOpening.Listener.SystemListener;
import de.Lukas.CaseOpening.System.Config.*;
import de.Lukas.CaseOpening.System.Config.Manager.ConfigManager;
import de.Lukas.CaseOpening.System.Database.Cases.CaseAmount;
import de.Lukas.CaseOpening.System.Database.Cases.CaseType;
import de.Lukas.CaseOpening.System.Database.Cases.CaseWin;
import de.Lukas.CaseOpening.System.Database.Credits.Credits;
import de.Lukas.CaseOpening.System.Database.DatabaseManager;
import de.Lukas.CaseOpening.System.Inventory.Objects.Inventories.*;
import de.Lukas.CaseOpening.System.Inventory.Manager.CaseInventoryManager;
import de.Lukas.CaseOpening.System.Inventory.Objects.CaseInventory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CaseOpening extends JavaPlugin {
    public static CaseOpening caseOpening;
    private static CaseInventoryManager caseInventoryManager;

    public static String prefix = "§7Case§5Opening §7>> ";
    @Override
    public void onEnable() {
        super.onEnable();
        caseOpening = this;

        initialize();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        DatabaseManager.disconnect();
    }

    public void initialize() {
        caseInventoryManager = new CaseInventoryManager(this);
        Config.loadConfigFile();

        registerDatabase();
        registerCommands();
        registerEvents();
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, this::registerInventories, 20);
    }

    public void registerCommands() {
        this.getCommand("spawn").setExecutor(new Spawn());
        this.getCommand("item").setExecutor(new Item());
        this.getCommand("case").setExecutor(new de.Lukas.CaseOpening.Command.Case());

    }

    public void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new SystemListener(), this);
        pluginManager.registerEvents(caseInventoryManager, this);
    }

    public void registerInventories() {
        new CaseMenuInventory();
        new LootInventory();
        new CaseLootInventory();
        new CaseLootInventory();
        new CasesInventory();
        new CaseBuyInventory();
    }

    public void registerDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String host = new ConfigManager(ConfigType.CONFIG, "HOST").stringValue();
        String db = new ConfigManager(ConfigType.CONFIG, "DATABASE").stringValue();
        String user = new ConfigManager(ConfigType.CONFIG, "USER").stringValue();
        String password = new ConfigManager(ConfigType.CONFIG, "PASSWORD").stringValue();
        String port = new ConfigManager(ConfigType.CONFIG, "PORT").stringValue();

        new DatabaseManager(host, port, db, user, password);

        CaseAmount.createTable();
        CaseType.createTable();
        Credits.createTable();
        CaseWin.createTable();
    }


    public static CaseInventoryManager getCaseInventoryManager() {
        return caseInventoryManager;
    }

    public static CaseInventory getCaseInventory(String searchName) {
        return (CaseInventory) (getCaseInventoryManager()).inventoryBySearchName.get(searchName);
    }

    public CaseOpening getCaseOpening() {
        return caseOpening;
    }
}