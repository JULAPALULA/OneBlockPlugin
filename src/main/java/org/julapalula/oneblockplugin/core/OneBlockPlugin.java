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
    public static ArrayList<Lot> arrayLot = new ArrayList<Lot>();
    public static OneBlockTask oneBlockTask = null;

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

        loadCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        logger.info("OneBlock Plugin (JULAPALULA) has been Disabled!");
    }
    private void loadCommands() {
        getCommand("lot").setExecutor(new OneBlockCommands(this));
    }
    private void registerListeners() {
       getServer().getPluginManager().registerEvents(new OneBlockListeners(this), this);
    }
    private void sendServerLog(String msg) {logger.info(msg);}

    /* Getters and setters */
    public void setOneBlockTask(OneBlockTask oneBlockTask) {this.oneBlockTask = oneBlockTask;}
    public  OneBlockTask getOneBlockTask() {return this.oneBlockTask;}

}
