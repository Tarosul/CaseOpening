package de.Lukas.CaseOpening.System.Entity;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class Firework {

    public static void spawnFirework(Player player) {
        Location loc = player.getLocation();
        org.bukkit.entity.Firework f = player.getWorld().spawn(loc, org.bukkit.entity.Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.AQUA).build());
        fm.setPower((int) 0.5);
        f.setFireworkMeta(fm);
    }
}
