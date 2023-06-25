package de.Lukas.CaseOpening.System.Inventory.Objects.Inventories;

import com.google.gson.JsonElement;
import de.Lukas.CaseOpening.CaseOpening;
import de.Lukas.CaseOpening.System.Base.ItemBuilder;
import de.Lukas.CaseOpening.System.Config.Manager.LootManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CaseLootInventory extends de.Lukas.CaseOpening.System.Inventory.Objects.CaseInventory {

    public static HashMap<String, String> cases = new HashMap<>();

    public CaseLootInventory() {
        super("caseloot", "Gewinnne", 6, true, true);
    }

    @Override
    public void onOpenInventory(Player player) {
        super.onOpenInventory(player);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 5.0f, 5.0f);
        String caseType = cases.get(player.getUniqueId().toString());
        for (int i = 0; i < LootManager.getCaseLootItems(caseType).size(); i++) {
            JsonElement jsonElement = LootManager.getCaseLootItems(caseType).get(i);
            Material mat = Material.valueOf(jsonElement.getAsJsonObject().get("item").getAsString());
            String name = jsonElement.getAsJsonObject().get("name").getAsString();
            ItemStack itemStack = new ItemBuilder(mat).setDisplayName(name.replace("&", "§")).build();
            if (name.contains("NONAME")) {
                itemStack = new ItemBuilder(mat).setDisplayName(itemStack.getItemMeta().getLocalizedName()).build();
            }
            setItem(i, itemStack);
        }
        setItem(45, new ItemBuilder("MHF_ARROWLEFT").setDisplayName("§c§lZurück").build());
    }

    @Override
    public void onClick(Player player, ItemStack paramItemStack) {
        switch (getClickedSlot()) {
            case 45 -> {
                CaseOpening.getCaseInventory("case").openInventory(player);
                player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_WOOD_DOOR_CLOSE, 0.5F, 1F);
            }
        }
    }
}
