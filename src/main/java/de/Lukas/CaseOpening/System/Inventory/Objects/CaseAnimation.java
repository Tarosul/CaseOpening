package de.Lukas.CaseOpening.System.Inventory.Objects;

import com.google.gson.JsonObject;
import de.Lukas.CaseOpening.CaseOpening;
import de.Lukas.CaseOpening.System.Base.ItemBuilder;
import de.Lukas.CaseOpening.System.Config.Manager.LootManager;
import de.Lukas.CaseOpening.System.Database.Cases.CaseWin;
import de.Lukas.CaseOpening.System.Entity.Firework;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class CaseAnimation implements Runnable {

    private Inventory inv;
    private Player player;
    private Thread thread;
    private int sched;

    private String inventoryName;
    private ArrayList<JsonObject> items = new ArrayList<>();

    public CaseAnimation(String caseType, Player player, Inventory inv) {
        this.player = player;
        this.thread = new Thread(this);
        this.inventoryName = player.getOpenInventory().getTitle();
        this.inv = inv;
        Inventory tmpInv = Bukkit.getServer().createInventory(inv.getHolder(), inv.getSize(), this.inventoryName);
        for (int i = 0; i < 10; i++) {
            JsonObject jsonObject = LootManager.getLoot(caseType);
            this.items.add(jsonObject);
        }
        this.inv = tmpInv;
        tmpInv.setItem(4, new ItemBuilder(Material.HOPPER).setDisplayName("§aDein Gewinn").build());
        this.startInventoryCheck();
        player.openInventory(tmpInv);
        player.updateInventory();
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(sched);
        thread.stop();
    }

    @Override
    public void run() {
        int i = 60;
        int random = new Random().nextInt(500, 510);

        while (player.isOnline()) {
            if (this.items.isEmpty())
                stop();
            this.checkInventory();
            i++;
            if (i > random) {
                JsonObject win = this.items.get(3);
                CaseWin.create(win, player.getUniqueId().toString());
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

                Bukkit.getScheduler().runTaskLater(CaseOpening.caseOpening, new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.dispatchCommand(console, win.get("cmd").getAsString().replace("/", "").replace("[PLAYER]", player.getName()));
                        Firework.spawnFirework(player);
                    }
                }, 10);
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 10.0f, 10.0f);
                stop();
            }
            if (i > 100 && i < 300) {
                i = 495;
            }
            for (int y = 9; y < 18; y++) {
                JsonObject item = this.items.get(y - 9);
                String name = item.get("name").getAsString();
                Material material = Material.valueOf(item.get("item").getAsString());
                ItemStack itemStack = new ItemBuilder(material).setDisplayName(name.replace("&", "§")).build();
                if (name.contains("NONAME")) {
                    itemStack = new ItemBuilder(material).setDisplayName(itemStack.getItemMeta().getLocalizedName()).build();
                }
                this.player.getOpenInventory().setItem(y, itemStack);
            }
            this.player.playSound(this.player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10.0f, 10.0f);
            this.items.add(this.items.get(0));
            this.items.remove(0);
            this.player.updateInventory();
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkInventory() {
        Bukkit.getScheduler().runTaskLater(CaseOpening.caseOpening, new Runnable() {
            @Override
            public void run() {
                if (player.getOpenInventory() == null) {
                    player.openInventory(inv);
                    start();
                } else {
                    if (!player.getOpenInventory().getTitle().equals(inventoryName)) {
                        player.openInventory(inv);
                    }
                }
            }
        }, 10);
    }

    private void startInventoryCheck() {
        sched = Bukkit.getScheduler().scheduleAsyncRepeatingTask(CaseOpening.caseOpening, () -> {
            if (player == null || !player.isOnline()) {
                stop();
            } else {
                checkInventory();
            }
        }, 0, 5);
    }
}
