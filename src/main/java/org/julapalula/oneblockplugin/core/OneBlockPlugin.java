package org.julapalula.oneblockplugin.core;
import org.bukkit.command.CommandExecutor;

import org.bukkit.plugin.java.JavaPlugin;
import org.julapalula.oneblockplugin.commands.OneBlockCommands;
import org.julapalula.oneblockplugin.listeners.OneBlockListeners;
import org.julapalula.oneblockplugin.playerinfo.PlayerData;
import org.julapalula.oneblockplugin.playerinfo.PlayerUnwrapper;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class OneBlockPlugin extends JavaPlugin {
    private final Logger logger = getLogger();

    private static ArrayList<Lot> arrayLot = new ArrayList<Lot>();
    private static ArrayList<PlayerData> arrayPlayer = new ArrayList<PlayerData>();

    @Override
    public void onEnable() {

        logger.info("OneBlock Plugin (JULAPALULA) has been Enabled!");
        // --- Unwrap lots
        logger.info("OneBlock Plugin (JULAPALULA) starting to unwrapping lots!");
        OneBlockMaterialUnwrapper unwrapper = new OneBlockMaterialUnwrapper();
        arrayLot = unwrapper.loadLots("one_block_data/lots");

        //Checks if it's empty
        if (arrayLot == null || arrayLot.isEmpty()) {
            logger.severe("No lots found! Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        logger.info("OneBlock Plugin (JULAPALULA) Unwrapping process finished with " + arrayLot.size()+ " lot(s) loaded!");

        for (Lot lot: arrayLot){
            logger.info("Lot "+ lot.getLotName() +" successfully loaded.");
        }

        // --- Unwrap player data
        logger.info("OneBlock Plugin (JULAPALULA) starting to unwrapping player data!");

        //Unwrap player data files
        PlayerUnwrapper player_unwrapper = new PlayerUnwrapper(arrayLot);
        arrayPlayer = player_unwrapper.loadPlayerData("one_block_data/player_data");

        logger.info("OneBlock Plugin (JULAPALULA) " +
                (!arrayPlayer.isEmpty()
                        ? "loaded data from " + arrayPlayer.size()
                        : "no players data founded!"));

        //TODO: Here should be the task
        loadCommands(arrayLot);
        registerListeners(arrayLot);
    }

    @Override
    public void onDisable() {
        logger.info("OneBlock Plugin (JULAPALULA) has been Disabled!");
    }

    private void loadCommands(ArrayList<Lot> arrayLot) {
        getCommand("lot").setExecutor(new OneBlockCommands(arrayLot));
    }

    private void registerListeners(ArrayList<Lot> arrayLot) {
       getServer().getPluginManager().registerEvents(new OneBlockListeners(this), this);
    }

    private void sendServerLog(String msg) {
        logger.info(msg);
    }
}
