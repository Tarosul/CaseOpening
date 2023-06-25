package de.Lukas.CaseOpening.System.Entity;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class ArmorStandBuilder {

    private Location loc;

    public ArmorStandBuilder(Location loc) {
        this.loc = loc;
    }

    public ArmorStandBuilder spawnHologram(String text) {
        String[] lines = text.split("\n");
        for (int x = 0; x < lines.length; x++) {
            double y = -(x * 0.25D);
            Location newLoc = this.loc.clone().add(0.0D, y, 0.0D);
            ArmorStand stand = (ArmorStand) newLoc.getWorld().spawn(newLoc, ArmorStand.class);
            stand.setCustomName(lines[x]);
            stand.setCustomNameVisible(true);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setSmall(true);
        }
        return this;
    }
}
