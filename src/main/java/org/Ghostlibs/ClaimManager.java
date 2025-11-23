package org.Ghostlibs;

import de.Main.database.DBM;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ClaimManager {

    DBM dbm;
    private static ClaimManager instance;
    public ClaimManager(DBM dbm){
        this.dbm = dbm;
    }

    public static ClaimManager getInstance() {
        if(instance == null) instance = new ClaimManager(Main.getInstance().getDbm());
        return instance;
    }



    public void giveReward(Player player) {

        Economy eco = Main.getInstance().getEconomy();
        int reward = dbm.getInt(Main.tableName, player.getUniqueId(), "reward", 0);
        eco.depositPlayer(player, reward);

        dbm.remove(Main.tableName, player.getUniqueId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("owner_uuid", player.getUniqueId().toString());
        data.put("nextClaimTime", getNextClaimTime());
        data.put("reward", 250);

        dbm.insertDefaultValues(Main.tableName, player.getUniqueId(), data);
    }

    public boolean canClaim(Player player){
        long currentTime = System.currentTimeMillis();
        long nextClaimTime = dbm.getLong(Main.tableName, player.getUniqueId(), "nextClaimTime", -1);
        if(nextClaimTime == -1) return false;
        if(currentTime >= nextClaimTime) return true;

        return false;
    }

    public String getRemainingTime(Player player){
        long currentTime = System.currentTimeMillis();
        long nextClaimTime = dbm.getLong(Main.tableName, player.getUniqueId(), "nextClaimTime", -1);
        if(nextClaimTime == -1) return "ERROR";

        long remainingMs = nextClaimTime - currentTime;
        long remainingSec = remainingMs / 1000;
        long remainingMin = remainingSec / 60;

        String remaining = remainingMin + " Minuten und " + remainingSec + " Sekunden";

        return remaining;
    }


    public long getNextClaimTime(){
        long nextClaimTime = System.currentTimeMillis() + (1 * 60 * 1000);
        return  nextClaimTime;
    }
}
