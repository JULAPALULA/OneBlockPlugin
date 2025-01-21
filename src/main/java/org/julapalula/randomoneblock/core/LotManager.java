package org.julapalula.randomoneblock.core;

import org.bukkit.entity.Player;
import org.julapalula.randomoneblock.playerinfo.PlayerData;
import org.julapalula.randomoneblock.playerinfo.PlayerUnwrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Manages player-specific lots in the OneBlockPlugin.
 *
 * <p>
 * This class provides methods to:
 * <ul>
 *   <li>Retrieve a lot by its name.</li>
 *   <li>Check if a lot is enabled for a player.</li>
 *   <li>Check if a lot has been purchased by a player.</li>
 *   <li>Determine the enabled status of multiple lots for a player.</li>
 * </ul>
 * </p>
 *
 * <strong>Usage:</strong>
 * <pre>
 * LotManager manager = new LotManager(plugin, player);
 * boolean isEnabled = manager.isLotEnabled("exampleLot");
 * </pre>
 *
 * <strong>Dependencies:</strong>
 * <ul>
 *   <li>{@code PlayerData}: Manages player-specific data.</li>
 *   <li>{@code PlayerUnwrapper}: Loads player data from the plugin.</li>
 *   <li>{@code Lot}: Represents a lot with specific attributes.</li>
 * </ul>
 */

public class LotManager {
    private Player player = null;
    private ROBPlugin plugin = null;
    private final PlayerUnwrapper player_unwrapper = new PlayerUnwrapper(plugin);

    /**
     * Constructs a LotManager instance.
     *
     * @param plugin the instance of OneBlockPlugin
     * @param player the player for whom the lots are being managed
     */

    public LotManager(ROBPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    /**
     * Retrieves a lot object by its name.
     *
     * @param lotName the name of the lot to retrieve
     * @return the {@link Lot} object if found, otherwise null
     */
    public Lot getLotByName(String lotName) {
        for (Lot lot : plugin.arrayLot) {
            if (lot.getLotName().equalsIgnoreCase(lotName)) {
                return lot;
            }
        }
        return null; // Return null if no matching lot is found
    }

    /**
     * Checks if a lot is enabled for the player.
     *
     * @param lotName the name of the lot to check
     * @return true if the lot is enabled for the player, false otherwise
     */
    public boolean isLotEnabled(String lotName) {
        PlayerData pld = player_unwrapper.loadSinglePlayerData(player.getUniqueId());
        ArrayList<Lot> enabledLots = pld.getEnabledLots();
        for (Lot lot : enabledLots) {
            if (lot.getLotName().equalsIgnoreCase(lotName)) {
                return true; // Lot is enabled
            }
        }
        return false; // Lot is not enabled
    }

    /**
     * Checks if a lot has been purchased by the player.
     *
     * @param lotName the name of the lot to check
     * @return true if the lot is bought, false otherwise
     */
    public boolean isLotBought(String lotName) {
        PlayerData pld = player_unwrapper.loadSinglePlayerData(player.getUniqueId());

        // Ensure poolLots is not null
        ArrayList<Lot> poolLots = pld.getLotPool();

        if (poolLots.isEmpty()) {
            return false;
        }

        // Now you can safely iterate over poolLots
        for (Lot lot : poolLots) {
            if (lot.getLotName().equalsIgnoreCase(lotName)) {
                return true;  // Lot is bought
            }
        }
        return false;  // Lot is not bought
    }

    /**
     * Checks the enabled status of a list of lots for the player.
     *
     * @param lots a list of {@link Lot} objects to check
     * @return a {@link HashMap} where the key is the lot name and the value indicates whether the lot is enabled
     */

    public HashMap<String, Boolean> areLotsEnabled(ArrayList<Lot> lots) {
        HashMap<String, Boolean> result = new HashMap<>();
        PlayerData playerData = player_unwrapper.loadSinglePlayerData(player.getUniqueId());

        // Get enabled lots for the player
        ArrayList<Lot> enabledLots = playerData.getEnabledLots();

        // Convert enabled lots to a set of lot names for fast lookup
        HashSet<String> enabledLotNames = new HashSet<>();
        for (Lot lot : enabledLots) {
            enabledLotNames.add(lot.getLotName().toLowerCase());
        }

        // Check if each lot is enabled
        for (Lot lot : lots) {
            String lotName = lot.getLotName().toLowerCase();
            result.put(lot.getLotName(), enabledLotNames.contains(lotName));
        }

        return result; // Return a map of lot names and their enabled status
    }

}
