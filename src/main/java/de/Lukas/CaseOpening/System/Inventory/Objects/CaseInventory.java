package de.Lukas.CaseOpening.System.Inventory.Objects;

import java.util.ArrayList;
import java.util.List;

import de.Lukas.CaseOpening.CaseOpening;
import de.Lukas.CaseOpening.System.Base.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class CaseInventory {
    private String searchName;

    private String invName;

    private int size;

    private Material color = Material.BLACK_STAINED_GLASS_PANE;

    private boolean fill;

    private Inventory clickedInventory;

    private InventoryClickEvent clickEvent;

    private Inventory inv = null;

    private List<ItemStack> items = new ArrayList<>();

    private int clickedSlot;

    private boolean withoutDisplayname = false;

    private boolean cancelled = true;

    private boolean local;

    private boolean isLeftClick = true;

    private boolean isShiftClick = false;

    public CaseInventory(String searchName, String invName, int size, boolean fill, boolean local) {
        this.searchName = searchName;
        this.invName = invName;
        this.size = size;
        this.fill = fill;
        this.local = local;
        setInventory(Bukkit.createInventory(null, size * 9, invName));
        setupLayout(this.inv, getFill());
        loadItems();
        (CaseOpening.getCaseInventoryManager()).inventoryName.add(invName);
        (CaseOpening.getCaseInventoryManager()).inventories.add(this);
        (CaseOpening.getCaseInventoryManager()).inventoryObject.put(invName, this);
        (CaseOpening.getCaseInventoryManager()).inventoryBySearchName.put(searchName, this);
    }

    public void onOpenInventory(Player player) {
    }

    public void loadItems() {
    }

    public abstract void onClick(Player player, ItemStack paramItemStack);


    public boolean isWithoutDisplayname() {
        return this.withoutDisplayname;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }


    public void setClickedInventory(Inventory clickedInventory) {
        this.clickedInventory = clickedInventory;
    }

    public InventoryClickEvent getClickEvent() {
        return this.clickEvent;
    }

    public void setClickEvent(InventoryClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public boolean getFill() {
        return this.fill;
    }

    public void setInventory(Inventory inv) {
        this.inv = inv;
    }

    public void setLeftClick(boolean leftClick) {
        this.isLeftClick = leftClick;
    }

    public void setShiftClick(boolean shiftClick) {
        this.isShiftClick = shiftClick;
    }

    public void setClickedSlot(int clickedSlot) {
        this.clickedSlot = clickedSlot;
    }

    public int getClickedSlot() {
        return this.clickedSlot;
    }

    public void clearInventory() {
        this.inv = Bukkit.createInventory(null, this.size * 9, this.invName);
        this.items.clear();
    }

    public Inventory getInventory() {
        return this.inv;
    }

    public Inventory getInventory(Player player) {
        setInventory(Bukkit.createInventory(null, this.size * 9, this.invName));
        setupLayout(this.inv, getFill());
        loadItems();
        onOpenInventory(player);
        setInventory(this.inv);
        return this.inv;
    }

    public void setItem(int slot, ItemStack item) {
        this.inv.setItem(slot, item);
        if (!this.items.contains(item)) this.items.add(item);
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public void openInventory(Player player) {
        setInventory(Bukkit.createInventory(null, this.size * 9, this.invName));
        setupLayout(this.inv, getFill());
        loadItems();
        onOpenInventory(player);
        player.openInventory(this.inv);
        setInventory(this.inv);
    }

    public void updateInventory() {
        clearInventory();
        setupLayout(this.inv, getFill());
        loadItems();
        setInventory(this.inv);
    }

    private void setupLayout(Inventory inv, boolean fill) {
        int x;
        if (fill) for (x = inv.getSize() - 9; x < inv.getSize(); x++)
            inv.setItem(x, (new ItemBuilder(color)).setNoName().build());
    }

}
