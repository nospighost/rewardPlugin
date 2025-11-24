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


                    ClaimManager.getInstance().canClaim(player, claimAble -> {

                        if (claimAble) {
                            player.sendMessage(Main.prefix + "§bDu kannst deine Belohnung abholen!");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        } else {
                            ClaimManager.getInstance().getRemainingTime(player, remaining -> {
                                player.sendMessage(Main.prefix + "§cDu kannst deine Belohnung in " + remaining + " abholen!");
                            });
                        }

                    });

                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, period);
    }
}
