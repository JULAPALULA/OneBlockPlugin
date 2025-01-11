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

public class PlayerManager {

    // --- Player Writting management methods
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
        jsonBuilder.append("  \"enabled_lots\": []\n");
        jsonBuilder.append("}");

        // Write the JSON string to the file
        try (FileWriter writer = new FileWriter(playerFile)) {
            writer.write(jsonBuilder.toString());
            System.out.println("[OneBlockPlugin]Player data written to file: " + playerFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("[OneBlockPlugin]Failed to write player data: " + e.getMessage());
        }
    }

    // --- Ensure that the Player's directory is ok
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

    // --- Method to check if the player file exists based on the player's UUID
    public boolean doesPlayerDataExist(Player player) {
        String dir = "one_block_data/player_data";
        UUID playerUUID = player.getUniqueId(); // Get the player's UUID
        File playerFile = new File(dir, playerUUID + ".json"); // Define the file path

        // Return true if the file exists, false otherwise
        return playerFile.exists();
    }

    // --- Method to rewrite the player's score
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

    // --- Add a lot to the player's enabled lots
    public void addLot(Player player, String lotName) {
        modifyLot(player, lotName, true);
    }

    // --- Remove a lot from the player's enabled lots
    public void removeLot(Player player, String lotName) {
        modifyLot(player, lotName, false);
    }

    // --- Internal method to modify the player's enabled lots
    private void modifyLot(Player player, String lotName, boolean add) {
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

            // Get the "enabled_lots" array
            JsonArray enabledLots = playerData.getAsJsonArray("enabled_lots");
            if (enabledLots == null) {
                enabledLots = new JsonArray();
                playerData.add("enabled_lots", enabledLots);
            }

            if (add) {
                // Add the lot if it's not already in the list
                JsonElement lotNameElement = new JsonParser().parse(lotName);
                if (!enabledLots.contains(lotNameElement)) {
                    enabledLots.add(lotName);
                    System.out.println("[OneBlockPlugin] Lot " + lotName + " added for player " + player.getName());
                } else {
                    System.out.println("[OneBlockPlugin] Lot " + lotName + " is already enabled for player " + player.getName());
                }
            } else {
                // Remove the lot if it exists in the list
                boolean removed = false;
                for (int i = 0; i < enabledLots.size(); i++) {
                    if (enabledLots.get(i).getAsString().equals(lotName)) {
                        enabledLots.remove(i);
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
