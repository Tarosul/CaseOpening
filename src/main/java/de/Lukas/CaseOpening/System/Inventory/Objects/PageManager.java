package de.Lukas.CaseOpening.System.Inventory.Objects;

import java.util.ArrayList;
import java.util.List;

import de.Lukas.CaseOpening.System.Base.ItemBuilder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PageManager {
    private List<?> pageList;

    private int itemsPerPage;

    public PageManager(List<?> pageList, int itemsPerPage) {
        this.pageList = pageList;
        this.itemsPerPage = itemsPerPage;
    }

    public boolean existNextPage(int page) {
        return !getItemsOfPage(page + 1).isEmpty();
    }

    public <x> List<?> getItemsOfPage(int page) {
        List<x> tempList = new ArrayList<>();
        int start = this.itemsPerPage * page - this.itemsPerPage;
        if (start >= this.pageList.size())
            return tempList;
        for (int x = start; x < this.pageList.size() &&
                x < this.itemsPerPage * page; x++)
            tempList.add((x)this.pageList.get(x));
        return tempList;
    }

    public void setPageSwitch(Inventory inv, int page, String exit, String previousPageName, int slotPreviousPage, String nextPageName, int slotNextPage, ItemStack noSwitch) {
        if (page == 1) {
            inv.setItem(slotPreviousPage, new ItemBuilder("MHF_ARROWLEFT").setDisplayName(exit).build());
        } else {
            inv.setItem(slotPreviousPage, new ItemBuilder("MHF_ARROWLEFT").setDisplayName(previousPageName).build());
        }
        if (existNextPage(page)) {
            inv.setItem(slotNextPage, new ItemBuilder("MHF_ARROWRIGHT").setDisplayName(nextPageName).build());
        } else {
            inv.setItem(slotNextPage, noSwitch);
        }
    }
}
