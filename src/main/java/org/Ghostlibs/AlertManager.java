package org.Ghostlibs;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AlertManager {

    public void startTask() {
        int cooldownMinutes = Main.getInstance().getConfig().getInt("alertCooldown");
        long period = cooldownMinutes * 60 * 20L;
        FileConfiguration config = Main.getInstance().getConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {

                    ClaimManager.getInstance().canClaimAsync(player, claimAble -> {

                        if (claimAble) {
                            player.sendMessage(Main.prefix + config.getString("alertMessage"));
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        } else {
                            ClaimManager.getInstance().getRemainingTimeAsync(player, remaining -> {
                                String message = config.getString("denyMessage").replace("%time%", remaining);
                                player.sendMessage(Main.prefix + message);
                            });
                        }

                    });

                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, period);
    }
}
