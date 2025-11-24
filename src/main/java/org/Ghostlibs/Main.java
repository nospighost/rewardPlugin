package org.Ghostlibs;

import co.aikar.commands.PaperCommandManager;
import de.Main.database.DBM;
import de.Main.database.SQLConnection;
import de.Main.database.SQLDataType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;


public final class Main extends JavaPlugin {
    public static String prefix = "§6§lReward §8» §7";
    SQLConnection connection;
    DBM dbm;
    FileConfiguration config;
    Economy eco;
    PaperCommandManager commandManager;
    public static Main instance;
    public static String tableName = "reward";

    public static Main getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();
        commandManager = new PaperCommandManager(this);
        if (!setupEconomy()) {
            getLogger().info("Vault wurde nicht gefunden, Plugin wird deaktiviert");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        registerDatabase();
        registerCommands();

        Bukkit.getPluginManager().registerEvents(new JoinManager(), this);
        if(!(getConfig().getInt("alertCooldown") == -1)){

            AlertManager alertManager = new AlertManager();
            alertManager.startTask();
        }
    }


    @Override
    public void onDisable() {
    }


    private void registerDatabase() {
        connection = new SQLConnection(getConfig().getString("database.host"),
                getConfig().getInt("database.port"),
                config.getString("database.database"),
                config.getString("database.user")
                , config.getString("database.password"));


        HashMap<String, SQLDataType> columns = new HashMap<>();
        columns.put("owner_uuid", SQLDataType.CHAR);
        columns.put("nextClaimTime", SQLDataType.LONG);
        columns.put("reward", SQLDataType.INT);

        dbm = new DBM(connection, tableName, columns);

    }


    public DBM getDbm() {
        return dbm;
    }


    private void registerCommands() {
        commandManager.registerCommand(new RewardCommand());
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }

    public Economy getEconomy() {
        return eco;
    }
}
