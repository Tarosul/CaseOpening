package de.Lukas.CaseOpening.System.Inventory.Objects.Inventories;

import com.google.gson.JsonObject;
import de.Lukas.CaseOpening.CaseOpening;
import de.Lukas.CaseOpening.System.Base.ItemBuilder;
import de.Lukas.CaseOpening.System.Config.Manager.CaseManager;
import de.Lukas.CaseOpening.System.Database.Cases.CaseAmount;
import de.Lukas.CaseOpening.System.Database.Cases.CaseType;
import de.Lukas.CaseOpening.System.Database.Credits.Credits;
import de.Lukas.CaseOpening.System.Inventory.Objects.CaseAnimation;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class CaseMenuInventory extends de.Lukas.CaseOpening.System.Inventory.Objects.CaseInventory {

    HashMap<Integer, JsonObject> cases = new HashMap<>();
    String credits;

    public CaseMenuInventory() {
        super("case", "§c§lKisten", 4, true, true);
    }

    @Override
    public void onOpenInventory(Player player) {
        super.onOpenInventory(player);
        credits = String.valueOf(Credits.getCredits(player.getUniqueId().toString()));

        for (int i = 0; i < CaseManager.getItems().size(); i++) {
            JsonObject item = CaseManager.getItems().get(i).getAsJsonObject();
            int slot = item.get("slot").getAsInt();
            String type = item.get("case_type").getAsString();
            String id = CaseType.getCaseType(type).get("id").getAsString();
            int amount = CaseAmount.getCases(player.getUniqueId().toString(), id);
            List<String> lore = List.of(item.get("lore").getAsString()
                    .replace("&", "§")
                    .replace("[AMOUNT]", String.valueOf(amount))
                    .split("/n"));
            String name = item.get("name").getAsString().replace("&", "§");
            String material = item.get("item").getAsString();

            JsonObject object = new JsonObject();
            object.addProperty("id", id);
            object.addProperty("amount", amount);
            object.addProperty("type", type);
            cases.put(slot, object);
            setItem(slot, new ItemBuilder(Material.valueOf(material))
                    .setDisplayName(name)
                    .addLoreList(lore)
                    .build());
        }

        setItem(30, new ItemBuilder(Material.CHEST).setDisplayName("§c§lDeine Kisten")
                .addLore("§7Klicke hier, um alle deine Kisten anzuzeigen").build());
        setItem(31, new ItemBuilder(Material.GOLD_INGOT).setDisplayName("§eDu hast §a§l" + credits + " Credits")
                .addLoreArray("§7Auf der Seite §etarosul.de §7kannst du Credits kaufen")
                .addLore("§7Klicke hier, um alle deine Kisten anzuzeigen").build());
        setItem(32, new ItemBuilder(Material.NETHER_STAR).setDisplayName("§6§lKisten kaufen")
                .addLore("§7Klicke hier, um Kisten zu kaufen").build());
    }

    @Override
    public void onClick(Player player, ItemStack paramItemStack) {
        int slot = getClickedSlot();
        String uuid = player.getUniqueId().toString();

        for (int caseSlot : cases.keySet()) {
            if (caseSlot == slot) {
                int amount = cases.get(slot).get("amount").getAsInt();
                String caseId = cases.get(slot).get("id").getAsString();
                String caseType = cases.get(slot).get("type").getAsString();
                checkCase(player, getClickEvent(), uuid, caseType, caseId, amount);
            }
        }

        switch (slot) {
            case 30 -> CaseOpening.getCaseInventory("cases").openInventory(player);
            case 32 -> CaseOpening.getCaseInventory("casebuy").openInventory(player);
        }
    }

    public static void checkCase(Player player, InventoryClickEvent clickEvent, String uuid, String caseType, String caseId, int amount) {
        if (clickEvent.isRightClick()) {
            CaseLootInventory.cases.put(uuid, caseType);
            CaseOpening.getCaseInventory("caseloot").openInventory(player);
        } else if (clickEvent.isLeftClick()) {
            if (amount < 1) {
                player.sendMessage(CaseOpening.prefix + "Du hast leider keine Cases mehr.");
                player.closeInventory();
                return;
            }
            Inventory inv = CaseOpening.getCaseInventory("loot").getInventory(player);
            new CaseAnimation(caseType, player, inv).start();
            CaseAmount.setCases(uuid, caseId, amount-1);
        }
    }
}
