package org.julapalula.randomoneblock.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.julapalula.randomoneblock.commands.ROBCommands;
import org.julapalula.randomoneblock.listeners.ROBListeners;
import org.julapalula.randomoneblock.tabs.LotTabCompleter;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class ROBPlugin extends JavaPlugin {
    private final Logger logger = getLogger();
    public static ArrayList<Lot> arrayLot = new ArrayList<Lot>();
    public static OneBlockTask oneBlockTask = null;

    @Override
    public void onEnable() {
        logger.info("Random One Block Plugin (JULAPALULA) has been Enabled!");
        // --- Unwrap lots-
        logger.info("Random One Block Plugin (JULAPALULA) starting to unwrapping lots!");
        ROBMaterialUnwrapper unwrapper = new ROBMaterialUnwrapper();
        arrayLot = unwrapper.loadLots("one_block_data/lots");

        //Checks if it's empty
        if (arrayLot == null || arrayLot.isEmpty()) {
            logger.severe("No lots found! Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        logger.info("Random One Block Plugin (JULAPALULA) Unwrapping process finished with " + arrayLot.size()+ " lot(s) loaded!");

        for (Lot lot: arrayLot){
            logger.info("Lot "+ lot.getLotName() +" successfully loaded.");
        }

        loadCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        logger.info("Random One Block Plugin (JULAPALULA) has been Disabled!");
    }
    private void loadCommands() {
        getCommand("lot").setExecutor(new ROBCommands(this));
        // Register tab completer
        getCommand("lot").setTabCompleter(new LotTabCompleter());
    }
    private void registerListeners() {
       getServer().getPluginManager().registerEvents(new ROBListeners(this), this);
    }

    /* Getters and setters */
    public void setOneBlockTask(OneBlockTask oneBlockTask) {this.oneBlockTask = oneBlockTask;}
    public  OneBlockTask getOneBlockTask() {return this.oneBlockTask;}

}
