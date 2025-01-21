package org.julapalula.randomoneblock.core;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.julapalula.randomoneblock.playerinfo.PlayerData;
import org.julapalula.randomoneblock.playerinfo.PlayerUnwrapper;
import org.julapalula.randomoneblock.utils.ROBRandom;

import java.util.ArrayList;
import java.util.List;
/**
 * Manages periodic tasks for the OneBlockPlugin, such as giving random items to players.
 *
 * <p>
 * This class provides methods to:
 * <ul>
 *   <li>Start a scheduled task to give random items to a player.</li>
 *   <li>Stop the scheduled task.</li>
 *   <li>Determine and distribute random items based on enabled lots.</li>
 * </ul>
 * </p>
 *
 * <strong>Usage:</strong>
 * <pre>
 * OneBlockTask task = new OneBlockTask(plugin, player);
 * task.startTask();
 * task.stopTask();
 * </pre>
 *
 * <strong>Dependencies:</strong>
 * <ul>
 *   <li>{@code PlayerData}: Contains player-specific data including enabled lots.</li>
 *   <li>{@code PlayerUnwrapper}: Utility for loading player data.</li>
 *   <li>{@code OneBlockRandom}: Utility for generating random numbers.</li>
 *   <li>{@code BukkitRunnable}: Used for scheduling tasks in the Bukkit environment.</li>
 *   <li>{@code Lot}: Represents a lot with attributes such as materials.</li>
 * </ul>
 */
public class OneBlockTask {
    private ROBPlugin plugin = null;
    private final ROBRandom random = new ROBRandom(
            (int) (System.currentTimeMillis() % Integer.MAX_VALUE) +
                    (int) (Runtime.getRuntime().freeMemory() % Integer.MAX_VALUE) +
                    (int) (System.nanoTime() % Integer.MAX_VALUE)
    );
    private BukkitRunnable task;
    private Player player = null;
    private final PlayerUnwrapper player_unwrapper = new PlayerUnwrapper(plugin);

    /**
     * Constructs a OneBlockTask instance.
     *
     * @param plugin the instance of OneBlockPlugin
     * @param player the player for whom the task is being managed
     */
    public OneBlockTask(ROBPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    /**
     * Starts a scheduled task to give random items to the player every 10 seconds.
     *
     * <p>
     * If the task is already running, it will not start a new instance.
     * </p>
     */
    public void startTask() {
        if (task != null) {
            //plugin.getLogger().info("Task is already running.");
            return; // Avoid starting the task again if it's already running
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                giveRandomItem(player);
            }
        };

        task.runTaskTimer(plugin, 1200, 1200); // 1200 ticks = 1 minute
        plugin.getLogger().info("Task started!");
    }

    /**
     * Stops the scheduled task if it is running.
     *
     * <p>
     * If no task is running, an appropriate message is logged.
     * </p>
     */

    public void stopTask() {
        if (task != null) {
            task.cancel();
            task = null; // Nullify the reference to allow restarting
            //plugin.getLogger().info("Task stopped!");
        } else {
            //plugin.getLogger().info("No task is running to stop.");
        }
    }

    /**
     * Gives a random item to the specified player based on their enabled lots.
     *
     * <p>
     * The method ensures that the player has enabled lots and valid materials to receive items from.
     * If no enabled lots or valid materials are found, an appropriate message is sent to the player.
     * </p>
     *
     * @param player the player to receive the random item
     */

    private void giveRandomItem(Player player) {
        //Take the player data
        PlayerData pld = player_unwrapper.loadSinglePlayerData(player.getUniqueId());

        // Array of enabled lots
        ArrayList<Lot> array_enabled_lots = pld.getEnabledLots();

        if (array_enabled_lots.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "[ROB]"+ChatColor.RED+"No enabled lots found. Cannot give a random item.");
            return;
        }
        // We take the material list of a random enabled lot
        Lot randomLot = array_enabled_lots.get(random.nextIntInRange(0, array_enabled_lots.size()));
        List<Material> material_list = randomLot.getMaterials();
        if (material_list.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "[ROB]"+ChatColor.RED+"The selected lot has no materials. Cannot give a random item.");
            return;
        }

        // Get a random material from the list
        Material randomMaterial = material_list.get(random.nextIntInRange(0, material_list.size()));

        // Check if the material is valid
        if (randomMaterial == null || !randomMaterial.isItem()) {
            player.sendMessage(ChatColor.YELLOW + "[ROB]"+ChatColor.RED+"Failed to get a valid material. Cannot give a random item.");
            return;
        }

        // Create an item stack and add it to the player's inventory
        ItemStack item = new ItemStack(randomMaterial, 1);
        player.getInventory().addItem(item);

        // Notify the player
        player.sendMessage(ChatColor.YELLOW + "[ROB]"+ChatColor.GREEN+"You've received a random item: " +ChatColor.DARK_RED+ randomMaterial.name() + ChatColor.GREEN +" from "+ ChatColor.GOLD+randomLot.getLotName());
    }
}
