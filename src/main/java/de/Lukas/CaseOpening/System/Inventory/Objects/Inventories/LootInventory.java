package de.Lukas.CaseOpening.System.Inventory.Objects.Inventories;

import de.Lukas.CaseOpening.System.Inventory.Objects.CaseInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LootInventory extends CaseInventory {

    public LootInventory() {
        super( "loot", "§c§lLoot", 3, true, true);
    }

    @Override
    public void onOpenInventory(Player player) {
        super.onOpenInventory(player);
    }

    @Override
    public void onClick(Player player, ItemStack paramItemStack) {
    }
}
