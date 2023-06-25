package de.Lukas.CaseOpening.System.Base;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {

    private static Map<String, CooldownManager> cooldowns = new HashMap<String, CooldownManager>();
    private long start;
    private final int timeInSeconds;
    private final String id;
    private final String cooldownName;

    public CooldownManager(String id, String cooldownName, int timeInSeconds){
        this.id = id;
        this.cooldownName = cooldownName;
        this.timeInSeconds = timeInSeconds;
    }

    public static boolean isInCooldown(String id, String cooldownName){
        if(getTimeLeft(id, cooldownName)>=1){
            return true;
        } else {
            stop(id, cooldownName);
            return false;
        }
    }

    private static void stop(String id, String cooldownName){
        CooldownManager.cooldowns.remove(id+cooldownName);
    }

    private static CooldownManager getCooldown(String id, String cooldownName){
        return cooldowns.get(id.toString()+cooldownName);
    }

    public static int getTimeLeft(String id, String cooldownName){
        CooldownManager cooldown = getCooldown(id, cooldownName);
        int f = -1;
        if(cooldown!=null){
            long now = System.currentTimeMillis();
            long cooldownTime = cooldown.start;
            int totalTime = cooldown.timeInSeconds;
            int r = (int) (now - cooldownTime) / 1000;
            f = (r - totalTime) * (-1);
        }
        return f;
    }

    public void start(){
        this.start = System.currentTimeMillis();
        cooldowns.put(this.id.toString()+this.cooldownName, this);
    }
}
