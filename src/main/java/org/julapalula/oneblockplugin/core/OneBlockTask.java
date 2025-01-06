package org.julapalula.oneblockplugin.core;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.julapalula.oneblockplugin.lots.MaterialLots;

import java.util.HashMap;
import java.util.Map;

public class OneBlockTask {
    private final Map<Player, BukkitRunnable> activeTasks = new HashMap<>();
    private final Map<Player, String> playerLotMap = new HashMap<>();

    public void startTask(Player player, String lotName, Runnable onError, Runnable onSuccess) {
        try {
            // Stop any existing task for the player
            stopTask(player);

            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        if (!player.isOnline()) {
                            cancel();
                            activeTasks.remove(player);
                            playerLotMap.remove(player);
                            return;
                        }

                        Material randomMaterial = MaterialLots.getRandomMaterialFromLote(lotName);
                        if (randomMaterial == null) {
                            cancel();
                            activeTasks.remove(player);
                            playerLotMap.remove(player);
                            if (onError != null) onError.run();
                            return;
                        }

                        // Add the item and call success callback
                        ItemStack item = new ItemStack(randomMaterial, 1);
                        player.getInventory().addItem(item);
                        if (onSuccess != null) onSuccess.run();
                    } catch (Exception e) {
                        Bukkit.getLogger().severe("Error during task execution for player " + player.getName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            };

            // Schedule the task to run every 10 seconds (200 ticks) indefinitely
            task.runTaskTimer(player.getServer().getPluginManager().getPlugin("OneBlockPlugin"), 200L, 200L);
            activeTasks.put(player, task);
            playerLotMap.put(player, lotName);

        } catch (Exception e) {
            Bukkit.getLogger().severe("Failed to start task for player " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopTask(Player player) {
        try {
            BukkitRunnable task = activeTasks.get(player);
            if (task != null && !task.isCancelled()) {
                task.cancel();  // Cancel the task if it's not already cancelled
            }
            activeTasks.remove(player); // Remove it from the map
            playerLotMap.remove(player); // Remove lot association
        } catch (Exception e) {
            Bukkit.getLogger().severe("Failed to stop task for player " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getSelectedLot(Player player) {
        return playerLotMap.get(player); // Return null if no lot is selected
    }

    public boolean hasActiveTask(Player player) {
        return activeTasks.containsKey(player);
    }
}
