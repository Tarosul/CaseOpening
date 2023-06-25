package de.Lukas.CaseOpening.System.Inventory.Objects.Inventories;

import com.google.gson.JsonObject;
import de.Lukas.CaseOpening.CaseOpening;
import de.Lukas.CaseOpening.System.Base.ItemBuilder;
import de.Lukas.CaseOpening.System.Database.Cases.CaseAmount;
import de.Lukas.CaseOpening.System.Database.Cases.CaseType;
import de.Lukas.CaseOpening.System.Inventory.Objects.CaseInventory;
import de.Lukas.CaseOpening.System.Inventory.Objects.PageManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class CasesInventory extends CaseInventory {

    public static HashMap<String, Integer> pagesCases = new HashMap<>();
    private HashMap<Integer, JsonObject> caseItems = new HashMap<>();

    public CasesInventory() {
        super("cases", "§c§lDeine Kisten", 4, true, true);
    }

    @Override
    public void onOpenInventory(Player player) {
        super.onOpenInventory(player);
        setContent(player);
        player.playSound(player.getLocation(), Sound.BLOCK_WOOD_STEP, 10.0f, 10.0f);
    }

    @Override
    public void onClick(Player player, ItemStack paramItemStack) {
        String name = paramItemStack.getItemMeta().getDisplayName();
        int slot = getClickedSlot();
        String uuid = player.getUniqueId().toString();

        for (int caseSlot : caseItems.keySet()) {
            if (caseSlot == slot) {
                String caseId = caseItems.get(slot).get("id").getAsString();
                String caseType = caseItems.get(slot).get("type").getAsString();
                int amount = CaseAmount.getCases(uuid, caseId);
                CaseMenuInventory.checkCase(player, getClickEvent(), uuid, caseType, caseId, amount);
            }
        }

        switch (name) {
            case "§aZurück" -> {
                CaseOpening.getCaseInventory("case").openInventory(player);
                player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_WOOD_DOOR_CLOSE, 0.5F, 1F);
            }
            case "§aNächste Seite" -> {
                int cPage = pagesCases.get(player.getUniqueId().toString());
                pagesCases.put(player.getUniqueId().toString(), cPage + 1);
                setContent(player);
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 0.5F, 1F);
            }
            case "§aVorherige Seite" -> {
                int cPage = pagesCases.get(player.getUniqueId().toString());
                pagesCases.put(player.getUniqueId().toString(), cPage - 1);
                setContent(player);
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 0.5F, 1F);
            }
        }
    }

    private void setContent(Player player) {
        caseItems.clear();
        ArrayList<JsonObject> cases = new ArrayList<>();
        for (int i = 0; i < CaseType.getCaseTypes().size(); i++) {
            JsonObject caseType = CaseType.getCaseTypes().get(i);
            for (int j = 0; j < CaseAmount.getCases(player.getUniqueId().toString(), caseType.get("id").getAsString()); j++) {
                cases.add(caseType);
            }
        }
        PageManager pageManager = new PageManager(cases, 27);

        int slot = 0;
        if (pagesCases.size() == 0) pagesCases.put(player.getUniqueId().toString(), 1);
        for (Object objItem : pageManager.getItemsOfPage(pagesCases.get(player.getUniqueId().toString()))) {
            JsonObject item = (JsonObject) objItem;
            String material = item.get("material").getAsString();
            String id = item.get("id").getAsString();
            String type = item.get("type").getAsString();

            setItem(slot, new ItemBuilder(Material.valueOf(material))
                    .setDisplayName("§7>> " + item.get("name").getAsString()
                            .replace("&", "§"))
                    .addLore("§bLinksklick§7, um deine Kisten zu sehen")
                    .addLore("§cRechtsklick§7, um den Kisteninhalt zu betrachten")
                    .build());

            JsonObject object = new JsonObject();
            object.addProperty("id", id);
            object.addProperty("type", type);
            caseItems.put(slot, object);

            slot++;
        }
        pageManager.setPageSwitch(getInventory(), pagesCases.get(player.getUniqueId().toString()), "§aZurück", "§aVorherige Seite", 27, "§aNächste Seite", 35, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build());
    }
}
