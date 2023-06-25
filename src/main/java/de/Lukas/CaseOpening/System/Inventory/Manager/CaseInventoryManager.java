package de.Lukas.CaseOpening.System.Inventory.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.Lukas.CaseOpening.CaseOpening;
import de.Lukas.CaseOpening.System.Inventory.Objects.CaseInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CaseInventoryManager implements Listener {
    public List<CaseInventory> inventories = new ArrayList<>();

    public List<String> inventoryName = new ArrayList<>();

    public Map<String, CaseInventory> inventoryObject = new HashMap<>();

    public Map<String, CaseInventory> inventoryBySearchName = new HashMap<>();

    public CaseInventoryManager(CaseOpening caseOpening) {}

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        if (e.getInventory() != null) {
            String invName = e.getView().getTitle();
            if (invName != null) {
                e.setCancelled(true);
                if (e.getClickedInventory() != null &&
                        e.getClickedInventory().equals(player.getInventory()))
                    e.setCancelled(false);
                if (e.getCurrentItem() != null) {
                    CaseInventory inventory = this.inventoryObject.get(invName);
                    if (!inventory.isWithoutDisplayname()) {
                        if (!e.getCurrentItem().hasItemMeta())
                            return;
                        if (!e.getCurrentItem().getItemMeta().hasDisplayName())
                            return;
                    }
                    ItemStack item = e.getCurrentItem();
                    inventory.setClickedInventory(e.getClickedInventory());
                    inventory.setLeftClick(e.isLeftClick());
                    inventory.setShiftClick(e.isShiftClick());
                    inventory.setClickedSlot(e.getSlot());
                    inventory.setClickEvent(e);
                    inventory.onClick(player, item);
                }
            }
        }
    }
}
