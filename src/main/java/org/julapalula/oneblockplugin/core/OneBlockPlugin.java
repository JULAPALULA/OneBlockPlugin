package org.julapalula.oneblockplugin.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.julapalula.oneblockplugin.commands.OneBlockCommands;
import org.julapalula.oneblockplugin.listeners.RandomItemListener;
import java.util.logging.Logger;

public final class OneBlockPlugin extends JavaPlugin {
    private final Logger logger = getLogger();
    private OneBlockTask oneBlockTask;

    @Override
    public void onEnable() {
        logger.info("OneBlock Plugin (JULAPALULA) has been Enabled!");
        registerListeners();
        loadCommands(); // Ensure commands are registered
    }

    @Override
    public void onDisable() {
        logger.info("OneBlock Plugin (JULAPALULA) has been Disabled!");
    }

    private void loadCommands() {
        getCommand("selectlot").setExecutor(new OneBlockCommands(oneBlockTask));
        getCommand("currentlot").setExecutor(new OneBlockCommands(oneBlockTask));
    }

    private void registerListeners() {
        logger.info("OneBlock Plugin (JULAPALULA) listening random items!");

        // Establish task to plugin
        this.oneBlockTask = new OneBlockTask();
        logger.info("OneBlock Plugin (JULAPALULA) created task One Block");

        // Register the listeners
        getServer().getPluginManager().registerEvents(new RandomItemListener(this), this);
    }
    public OneBlockTask getOneBlockTask() {
        return oneBlockTask;
    }
}
