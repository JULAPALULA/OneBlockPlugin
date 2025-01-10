package org.julapalula.oneblockplugin.playerinfo;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

}
