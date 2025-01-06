package org.julapalula.oneblockplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;
import org.julapalula.oneblockplugin.core.OneBlockTask;

public class RandomItemListener implements Listener {
    private final OneBlockPlugin plugin;

    public RandomItemListener(OneBlockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Welcome! You'll start receiving random items after waiting for the task to begin.");

        // Use the task manager from the plugin
        OneBlockTask taskManager = plugin.getOneBlockTask();

        taskManager.startTask(
                player,
                "basic",
                () -> player.sendMessage("Error: Lot 'basic' does not exist!"),
                () -> player.sendMessage("You've received a random item!")
        );
    }
}
