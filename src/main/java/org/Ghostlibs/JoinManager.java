package org.Ghostlibs;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class JoinManager implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HashMap<String, Object> data = new HashMap<>();
        data.put("owner_uuid", player.getUniqueId().toString());
        data.put("nextClaimTime", System.currentTimeMillis());
        data.put("reward", 250);
        Main.getInstance().getDbm().insertDefaultValues(Main.tableName, player.getUniqueId(), data);


        ClaimManager.getInstance().getRemainingTimeAsync(player, remaining -> {

            if(Integer.valueOf(remaining) < 0){
                remaining = "";
            }


            if(Integer.valueOf(remaining) <= 0){
                player.sendMessage(Main.prefix);
                player.sendMessage(Main.prefix + "§bDu kannst deine Belohnung abholen!");
                player.sendMessage(Main.prefix);
            } else {
                player.sendMessage(Main.prefix);
                player.sendMessage(Main.prefix + "§bDu kannst deine Belohnung in " + remaining  + " abholen!");
                player.sendMessage(Main.prefix);
            }


        });

    }


}
