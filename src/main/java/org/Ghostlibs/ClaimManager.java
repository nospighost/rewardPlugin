package org.Ghostlibs;

import de.Main.database.DBM;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.Consumer;

public class ClaimManager {

    DBM dbm;
    private static ClaimManager instance;

    public ClaimManager(DBM dbm) {
        this.dbm = dbm;
    }

    public static ClaimManager getInstance() {
        if (instance == null) instance = new ClaimManager(Main.getInstance().getDbm());
        return instance;
    }


    public void giveReward(Player player) {

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

            int reward = dbm.getInt(Main.tableName, player.getUniqueId(), "reward", 0);

            dbm.remove(Main.tableName, player.getUniqueId());

            HashMap<String, Object> data = new HashMap<>();
            data.put("owner_uuid", player.getUniqueId().toString());
            data.put("nextClaimTime", getNextClaimTime());
            data.put("reward", 250);

            dbm.insertDefaultValues(Main.tableName, player.getUniqueId(), data);

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {

                Economy eco = Main.getInstance().getEconomy();
                eco.depositPlayer(player, reward);

            });
        });
    }


    public void canClaim(Player player, Consumer<Boolean> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

            long currentTime = System.currentTimeMillis();
            long nextClaimTime = dbm.getLong(Main.tableName, player.getUniqueId(), "nextClaimTime", -1);

            boolean result = nextClaimTime != -1 && currentTime >= nextClaimTime;

            Bukkit.getScheduler().runTask(Main.getInstance(), () ->
                    callback.accept(result)
            );
        });
    }


    public void getRemainingTime(Player player, Consumer<String> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

            long currentTime = System.currentTimeMillis();
            long nextClaimTime = dbm.getLong(Main.tableName, player.getUniqueId(), "nextClaimTime", -1);

            if (nextClaimTime == -1) {
                Bukkit.getScheduler().runTask(Main.getInstance(), () ->
                        callback.accept("ERROR")
                );
                return;
            }

            long remainingMs = nextClaimTime - currentTime;
            long remainingSec = remainingMs / 1000;
            long remainingMin = remainingSec / 60;

            String remaining = remainingMin + " Minuten und " + (remainingSec % 60) + " Sekunden";

            Bukkit.getScheduler().runTask(Main.getInstance(), () ->
                    callback.accept(remaining)
            );

        });
    }



    public long getNextClaimTime() {
        long nextClaimTime = System.currentTimeMillis() + (15 * 60 * 1000);
        return nextClaimTime;
    }
}
