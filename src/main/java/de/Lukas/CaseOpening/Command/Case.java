package de.Lukas.CaseOpening.Command;

import de.Lukas.CaseOpening.System.Config.Manager.CaseManager;
import de.Lukas.CaseOpening.System.Config.Manager.LootManager;
import de.Lukas.CaseOpening.System.Database.Cases.CaseAmount;
import de.Lukas.CaseOpening.System.Database.Cases.CaseType;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Case implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (CaseType.isCreated(args[0])) {
            sender.sendMessage("§7Die Case vom Typ " + args[0] + " existiert bereits!");
            return true;
        }
        try {
            CaseType.create(args[0], args[1], Material.valueOf(args[2]));
            LootManager.saveItem(args[0], null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String lore = "&7>> Du hast &b[AMOUNT]x &a" + args[1] + "&7-§aKiste /n&bLinksklick&7, um deine Kiste zu öffnen /n&cRechtsklick &7um den Kisteninhalt zu betrachten";
        CaseAmount.register(player.getUniqueId().toString(), CaseType.getCaseType(args[0]).get("id").getAsString(), 20);
        String caseSlot = String.valueOf(CaseManager.getItems().size());
        CaseManager.addItem(args[0], args[2], args[1], lore, caseSlot, args[3]);
        sender.sendMessage("§7Du hast erfolgreich die §aCase §7" + args[1] + "( " + args[0],args[2] + " ) hinzugefügt.");
        return false;
    }
}
