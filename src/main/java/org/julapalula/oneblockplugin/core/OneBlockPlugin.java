package org.julapalula.oneblockplugin.core;
import org.bukkit.command.CommandExecutor;

import org.bukkit.plugin.java.JavaPlugin;
import org.julapalula.oneblockplugin.commands.OneBlockCommands;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class OneBlockPlugin extends JavaPlugin {
    private final Logger logger = getLogger();

    @Override
    public void onEnable() {
        ArrayList<Lot> arrayLot = new ArrayList<Lot>();
        logger.info("OneBlock Plugin (JULAPALULA) has been Enabled!");
        // --- Unwrap lots
        logger.info("OneBlock Plugin (JULAPALULA) starting to unwrapping lots!");
        OneBlockMaterialUnwrapper unwrapper = new OneBlockMaterialUnwrapper();
        arrayLot = unwrapper.loadLods("lots");

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

        //TODO: Here should be the task
        loadCommands(arrayLot);
        registerListeners(arrayLot);
    }

    @Override
    public void onDisable() {
        logger.info("OneBlock Plugin (JULAPALULA) has been Disabled!");
    }

    private void loadCommands(ArrayList<Lot> arrayLot) {
        getCommand("selectlot").setExecutor(new OneBlockCommands(arrayLot));
    }

    private void registerListeners(ArrayList<Lot> arrayLot) {
       // getServer().getPluginManager().registerEvents(new RandomItemListener(this), this);
    }

    private void sendServerLog(String msg) {
        logger.info(msg);
    }
}
