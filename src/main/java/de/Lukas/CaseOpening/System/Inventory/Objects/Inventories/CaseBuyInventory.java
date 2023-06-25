package de.Lukas.CaseOpening.System.Inventory.Objects.Inventories;

import com.google.gson.JsonObject;
import de.Lukas.CaseOpening.CaseOpening;
import de.Lukas.CaseOpening.System.Base.CooldownManager;
import de.Lukas.CaseOpening.System.Base.ItemBuilder;
import de.Lukas.CaseOpening.System.Config.Manager.CaseManager;
import de.Lukas.CaseOpening.System.Database.Cases.CaseAmount;
import de.Lukas.CaseOpening.System.Database.Cases.CaseType;
import de.Lukas.CaseOpening.System.Database.Credits.Credits;
import de.Lukas.CaseOpening.System.Inventory.Objects.CaseInventory;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CaseBuyInventory extends CaseInventory {
    HashMap<Integer, JsonObject> cases = new HashMap<>();

    public CaseBuyInventory() {
        super("casebuy", "§c§lKisten kaufen", 6, true, true);
    }

    @Override
    public void onOpenInventory(Player player) {
        super.onOpenInventory(player);
        player.playSound(player.getLocation(), Sound.BLOCK_WOOD_STEP, 10.0f, 10.0f);
        int slotMulti = 0;
        int chestAmount = 4;
        String lore = "§7Klicke, um §6AMOUNT §7Kisten für §6PRICE §7zu kaufen";

        for (int i = 0; i < CaseManager.getItems().size(); i++) {
            JsonObject item = CaseManager.getItems().get(i).getAsJsonObject();
            String type = item.get("case_type").getAsString();
            String name = item.get("name").getAsString().replace("&", "§");
            Material material = Material.valueOf(item.get("item").getAsString());
            int typePrice = item.get("price").getAsInt();

            for (int j = 1; j < 5; j++) {
                ItemStack itemStack;
                int tmpSlot = j*2+slotMulti-1;
                int amount;
                int casePrice = typePrice;
                if (j == 1) {
                    amount = 1;
                } else {
                    amount = j * chestAmount;
                    casePrice = j * typePrice;
                }
                itemStack = new ItemBuilder(material)
                        .setDisplayName(name)
                        .addLore(lore.replace("AMOUNT", String.valueOf(amount))
                                .replace("PRICE", String.valueOf(casePrice)))
                        .build();

                setItem(tmpSlot, itemStack);
                JsonObject object = new JsonObject();
                object.addProperty("type", type);
                object.addProperty("amount", amount);
                object.addProperty("price", casePrice);
                cases.put(tmpSlot, object);
            }
            slotMulti = slotMulti + 9;
        }
        setItem(45, new ItemBuilder("MHF_ARROWLEFT").setDisplayName("§c§lZurück").build());
    }

    @Override
    public void onClick(Player player, ItemStack paramItemStack) {
        int clickSlot = getClickedSlot();
        String uuid = player.getUniqueId().toString();
        for (int caseSlot : cases.keySet()) {
            if (caseSlot == clickSlot) {
                JsonObject buyCase = cases.get(caseSlot).getAsJsonObject();
                int price = buyCase.get("price").getAsInt();
                int amount = buyCase.get("amount").getAsInt();
                String type = buyCase.get("type").getAsString();
                if (CooldownManager.isInCooldown(uuid, "buy")) {
                    player.sendMessage(CaseOpening.prefix + "Bitte warte noch " + CooldownManager.getTimeLeft(uuid, "buy") + " Sekunden.");
                    return;
                }
                int credits = Credits.getCredits(uuid);
                if (!(credits >= price)) {
                    player.sendMessage(CaseOpening.prefix + "Du hast nicht genügend Credits.");
                    return;
                }
                String caseId = CaseType.getCaseType(type).get("id").getAsString();
                CaseAmount.setCases(uuid, caseId, CaseAmount.getCases(uuid, caseId) + amount);
                Credits.setCredits(credits-price, uuid);
                new CooldownManager(uuid, "buy", 3).start();
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0f, 10.0f);
            }
        }
        switch (clickSlot) {
            case 45 -> {
                CaseOpening.getCaseInventory("case").openInventory(player);
                player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_WOOD_DOOR_CLOSE, 0.5F, 1F);
            }
        }
    }
}
