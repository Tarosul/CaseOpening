package de.Lukas.CaseOpening.Command;

import de.Lukas.CaseOpening.System.Base.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        player.getInventory().addItem(new ItemBuilder(Material.ENDER_CHEST).setDisplayName("§5CaseOpening").setGlow().build());
        return false;
    }
}
