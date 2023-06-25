package de.Lukas.CaseOpening.Listener;

import com.google.gson.JsonObject;
import de.Lukas.CaseOpening.CaseOpening;
import de.Lukas.CaseOpening.System.Config.Manager.CaseManager;
import de.Lukas.CaseOpening.System.Database.Cases.CaseAmount;
import de.Lukas.CaseOpening.System.Database.Cases.CaseType;
import de.Lukas.CaseOpening.System.Database.Credits.Credits;
import de.Lukas.CaseOpening.System.Entity.ArmorStandBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class SystemListener implements Listener {

    @EventHandler
    public void place(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        if (item.hasItemMeta()) if (item.getItemMeta().hasDisplayName())
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§5CaseOpening")) {
                player.sendMessage(CaseOpening.prefix + "Du hast das §5CaseOpening §7erstellt!");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0f, 10.0f);
                new ArmorStandBuilder(event.getBlockPlaced().getLocation().add(0.5, 0.5, 0.5)).spawnHologram("§6Deine Kisten \n §7Tolle Sachen gewinnen!");
            }
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage("");
        if (!Credits.isRegistered(player.getUniqueId().toString())) {
            Credits.register(player.getUniqueId().toString(), 50000);
        }
        if (!CaseAmount.isRegistered(player.getUniqueId().toString())) {
            for (int i = 0; i < CaseManager.getItems().size(); i++) {
                JsonObject item = CaseManager.getItems().get(i).getAsJsonObject();
                String type = item.get("case_type").getAsString();
                String id = CaseType.getCaseType(type).get("id").getAsString();
                CaseAmount.register(player.getUniqueId().toString(), id, 10);
            }
        }
    }

    @EventHandler
    public void click(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
            Location location = block.getLocation();
            for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
                if (entity instanceof ArmorStand) {
                    event.setCancelled(true);
                    CaseOpening.getCaseInventory("case").openInventory(event.getPlayer());
                }
            }
        }
    }
}
