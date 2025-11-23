package org.Ghostlibs;


import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AlertManager {

    public void startTask() {
        int cooldownMinutes = Main.getInstance().getConfig().getInt("alertCooldown");
        long period = cooldownMinutes * 60 * 20L;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (ClaimManager.getInstance().canClaim(player)) {
                        player.sendMessage(Main.prefix + "§bDu kannst deine Belohnung abholen!");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    } else {
                        player.sendMessage(Main.prefix + "§cDu kannst deine Belohnung in " + ClaimManager.getInstance().getRemainingTime(player) + " abholen!");
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, period);
    }
}
