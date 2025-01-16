package org.julapalula.oneblockplugin.playerinfo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.entity.Player;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
/**
 * Manages player-specific data for the OneBlockPlugin.
 *
 * <p>
 * This class provides methods to:
 * <ul>
 *   <li>Create and manage player data files.</li>
 *   <li>Modify player scores.</li>
 *   <li>Add or remove lots in the player's enabled or pool lists.</li>
 *   <li>Check for the existence of player data files.</li>
 * </ul>
 * </p>
 *
 * <strong>Usage:</strong>
 * <pre>
 * PlayerManager manager = new PlayerManager();
 * manager.createPlayerData(player);
 * manager.addLotEnabled(player, "exampleLot");
 * </pre>
 *
 * <strong>Dependencies:</strong>
 * <ul>
 *   <li>{@code Player}: Represents a player in the Bukkit API.</li>
 *   <li>{@code JsonObject}, {@code JsonArray}: Used for managing JSON player data.</li>
 * </ul>
 */
public class PlayerManager {

    /**
     * Creates a new JSON file to store data for a player.
     *
     * @param player the player for whom the data is being created
     */

    public void createPlayerData(Player player) {

        // Define the directory for player data
        String dir = "one_block_data/player_data";
        UUID playerUUID = player.getUniqueId(); // Assuming Player has a getUniqueId() method

        ensurePlayerDirectoryCreated(dir);
        // Define the JSON file for the specific player's data
        File playerFile = new File(dir, playerUUID + ".json");

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("  \"score\": 0,\n");
        jsonBuilder.append("  \"enabled_lots\": [],\n");
        jsonBuilder.append("  \"lot_pool\": []\n");
        jsonBuilder.append("}");

        // Write the JSON string to the file
        try (FileWriter writer = new FileWriter(playerFile)) {
            writer.write(jsonBuilder.toString());
            System.out.println("[OneBlockPlugin]Player data written to file: " + playerFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("[OneBlockPlugin]Failed to write player data: " + e.getMessage());
        }
    }

    /**
     * Ensures that the directory for player data exists.
     *
     * @param dir the directory path
     */

    private void ensurePlayerDirectoryCreated(String dir) {
        // Ensure the directory exists
        File directory = new File(dir);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("[OneBlockPlugin]Directory created: " + dir);
            } else {
                System.out.println("[OneBlockPlugin]Failed to create directory: " + dir);
                return;
            }
        }
    }

    /**
     * Checks if a data file exists for the given player.
     *
     * @param player the player to check
     * @return true if the data file exists, false otherwise
     */

    public boolean doesPlayerDataExist(Player player) {
        String dir = "one_block_data/player_data";
        UUID playerUUID = player.getUniqueId(); // Get the player's UUID
        File playerFile = new File(dir, playerUUID + ".json"); // Define the file path

        // Return true if the file exists, false otherwise
        return playerFile.exists();
    }

    /**
     * Updates the player's score in their data file.
     *
     * @param player   the player whose score is being updated
     * @param newScore the new score to set
     */

    public void rewritePlayerScore(Player player, int newScore) {
        String dir = "one_block_data/player_data";
        UUID playerUUID = player.getUniqueId();
        File playerFile = new File(dir, playerUUID + ".json");

        if (!playerFile.exists()) {
            System.out.println("[OneBlockPlugin] Player data file not found for UUID: " + playerUUID);
            return;
        }

        try {
            // Read the existing JSON data
            JsonObject playerData;
            try (Reader reader = new FileReader(playerFile)) {
                playerData = JsonParser.parseReader(reader).getAsJsonObject();
            }

            // Update the score
            playerData.addProperty("score", newScore);

            // Write the updated data back to the file
            try (FileWriter writer = new FileWriter(playerFile)) {
                writer.write(playerData.toString());
                System.out.println("[OneBlockPlugin] Player score updated to " + newScore + " for UUID: " + playerUUID);
            }
        } catch (IOException e) {
            System.out.println("[OneBlockPlugin] Failed to rewrite player score: " + e.getMessage());
        }
    }

    /**
     * Adds a lot to the player's lot pool.
     *
     * @param player  the player whose lot pool is being updated
     * @param lotName the name of the lot to add
     */
    public void addLotPool(Player player, String lotName) {
        modifyLot(player, lotName, true, true);
    }

    /**
     * Removes a lot from the player's lot pool.
     *
     * @param player  the player whose lot pool is being updated
     * @param lotName the name of the lot to remove
     */
    public void removeLotPool(Player player, String lotName) {
        modifyLot(player, lotName, false, true);
    }

    /**
     * Adds a lot to the player's enabled lots.
     *
     * @param player  the player whose enabled lots are being updated
     * @param lotName the name of the lot to add
     */
    public void addLotEnabled(Player player, String lotName) {
        modifyLot(player, lotName, true, false);
    }

    /**
     * Removes a lot from the player's enabled lots.
     *
     * @param player  the player whose enabled lots are being updated
     * @param lotName the name of the lot to remove
     */
    public void removeLotEnabled(Player player, String lotName) {
        modifyLot(player, lotName, false, false);
    }


    /**
     * Modifies a player's lot pool or enabled lots.
     *
     * @param player   the player whose data is being modified
     * @param lotName  the name of the lot to modify
     * @param add      true to add the lot, false to remove it
     * @param isToPool true if modifying the lot pool, false for enabled lots
     */
    private void modifyLot(Player player, String lotName, boolean add, boolean isToPool) {
        String dir = "one_block_data/player_data";
        UUID playerUUID = player.getUniqueId();

        File playerFile = new File(dir, playerUUID + ".json");

        if (!playerFile.exists()) {
            System.out.println("[OneBlockPlugin] Player data file not found for UUID: " + playerUUID);
            return;
        }

        try {
            // Read the existing JSON data
            JsonObject playerData;
            try (Reader reader = new FileReader(playerFile)) {
                playerData = JsonParser.parseReader(reader).getAsJsonObject();
            }

            // Get the "enabled_lots" or "lot_pool" array
            String json_aim = isToPool ? "lot_pool" : "enabled_lots";

            JsonArray JSONLotArray = playerData.getAsJsonArray(json_aim);

            if (JSONLotArray == null) {
                JSONLotArray = new JsonArray();
                playerData.add(json_aim, JSONLotArray);
            }

            if (add) {
                // Add the lot if it's not already in the list
                JsonElement lotNameElement = new JsonParser().parse(lotName);
                if (!JSONLotArray.contains(lotNameElement)) {
                    JSONLotArray.add(lotName);
                    System.out.println("[OneBlockPlugin] Lot " + lotName + " added for player " + player.getName());
                } else {
                    System.out.println("[OneBlockPlugin] Lot " + lotName + " is already enabled for player " + player.getName());
                }
            } else {
                // Remove the lot if it exists in the list
                boolean removed = false;
                for (int i = 0; i < JSONLotArray.size(); i++) {
                    if (JSONLotArray.get(i).getAsString().equals(lotName)) {
                        JSONLotArray.remove(i);
                        removed = true;
                        break;
                    }
                }

                if (removed) {
                    System.out.println("[OneBlockPlugin] Lot " + lotName + " removed for player " + player.getName());
                } else {
                    System.out.println("[OneBlockPlugin] Lot " + lotName + " not found for player " + player.getName());
                }
            }

            // Write the updated data back to the file
            try (FileWriter writer = new FileWriter(playerFile)) {
                writer.write(playerData.toString());
            }
        } catch (IOException e) {
            System.out.println("[OneBlockPlugin] Failed to modify lot: " + e.getMessage());
        }
    }
}
