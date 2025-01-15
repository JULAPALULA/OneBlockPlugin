package org.julapalula.oneblockplugin.core;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.julapalula.oneblockplugin.playerinfo.PlayerData;
import org.julapalula.oneblockplugin.playerinfo.PlayerUnwrapper;
import org.julapalula.oneblockplugin.utils.OneBlockRandom;

import java.util.ArrayList;
import java.util.List;

public class OneBlockTask {
    private OneBlockPlugin plugin = null;
    private final OneBlockRandom random = new OneBlockRandom(5489); //54
    private BukkitRunnable task;
    private Player player = null;
    private final PlayerUnwrapper player_unwrapper = new PlayerUnwrapper(plugin);

    // Constructor to initialize with the plugin instance
    public OneBlockTask(OneBlockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    // Start the task to give random items
    public void startTask() {
        if (task != null) {
            plugin.getLogger().info("Task is already running.");
            return; // Avoid starting the task again if it's already running
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                giveRandomItem(player);
            }
        };

        task.runTaskTimer(plugin, 200, 200); // 200 ticks = 10 seconds
        plugin.getLogger().info("Task started!");
    }

    // Stop the task
    public void stopTask() {
        if (task != null) {
            task.cancel();
            task = null; // Nullify the reference to allow restarting
            plugin.getLogger().info("Task stopped!");
        } else {
            plugin.getLogger().info("No task is running to stop.");
        }
    }

    // Method to give a random item to a player
    private void giveRandomItem(Player player) {
        //Take the player data
        PlayerData pld = player_unwrapper.loadSinglePlayerData(player.getUniqueId());

        // Array of enabled lots
        ArrayList<Lot> array_enabled_lots = pld.getEnabledLots();
        player.sendMessage("Size:" + array_enabled_lots.size());

        if (array_enabled_lots.isEmpty()) {
            player.sendMessage("No enabled lots found. Cannot give a random item.");
            return;
        }
        // We take the material list of a random enabled lot
        Lot randomLot = array_enabled_lots.get(random.nextIntInRange(0, array_enabled_lots.size()));
        List<Material> material_list = randomLot.getMaterials();
        if (material_list.isEmpty()) {
            player.sendMessage("The selected lot has no materials. Cannot give a random item.");
            return;
        }

        // Get a random material from the list
        Material randomMaterial = material_list.get(random.nextIntInRange(0, material_list.size()));

        // Check if the material is valid
        if (randomMaterial == null || !randomMaterial.isItem()) {
            player.sendMessage("Failed to get a valid material. Cannot give a random item.");
            return;
        }

        // Create an item stack and add it to the player's inventory
        ItemStack item = new ItemStack(randomMaterial, 1);
        player.getInventory().addItem(item);

        // Notify the player
        player.sendMessage("You've received a random item: " + randomMaterial.name() + " from "+ randomLot.getLotName());
    }
}
