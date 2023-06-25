package de.Lukas.CaseOpening.Command;

import de.Lukas.CaseOpening.System.Config.Manager.LootManager;
import de.Lukas.CaseOpening.System.Database.Cases.CaseType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Item implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!CaseType.isCreated(args[0])) {
            sender.sendMessage("§7Die Case vom Typ " + args[0] + " existiert noch nicht. Erstelle zuerst eine!");
            return true;
        }
        StringBuilder cmd = new StringBuilder();
        cmd.append(args[4]);
        for (int i = 5; i < args.length; i++) {
            cmd.append(" ").append(args[i]);
        }
        LootManager.addItem(args[0], args[1], args[2], args[3], cmd.toString());
        sender.sendMessage("§7Du hast erfolgreich einen §aneuen §7Gegenstand hinzugefügt.");
        return false;
    }

}
