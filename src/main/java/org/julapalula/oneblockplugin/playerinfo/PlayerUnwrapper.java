package org.julapalula.oneblockplugin.playerinfo;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.julapalula.oneblockplugin.core.Lot;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;
import org.julapalula.oneblockplugin.utils.OneBlockFileManagerUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PlayerUnwrapper {
    OneBlockFileManagerUtil fileManager = new OneBlockFileManagerUtil();
    JSONParser parser = new JSONParser();
    OneBlockPlugin plugin = null;

    public PlayerUnwrapper(OneBlockPlugin plugin) {
        this.plugin = plugin;
    }

     /**
     * Get the list of [UUID].json files in the specified directory.
     *
     * @param directoryPath the path to the directory
     * @return a list of File objects representing [UUID].json files
     */

    private List<File> getPlayersFiles(String directoryPath) {
        File folder = new File(directoryPath);

        // List .lot.json files in the directory
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json") && !fileManager.hasSpecialCharacters(fileManager.getFileNameWithoutExtension(name)));

        if (files != null && files.length > 0) {
            System.out.println("[OneBlockPlugin] Founded " + files.length + " players data.");
            return Arrays.asList(files); // Convert the File[] array to a List<File>
        } else {
            System.out.println("[OneBlockPlugin] No players data files found in the directory: " + folder.getAbsolutePath());
            return new ArrayList<>(); // Return an empty list if no files are found
        }
    }

    /**
     * Get the list of [UUID].json file in the specified directory.
     *
     * @param directoryPath the path to the directory
     * @return a list of File objects representing [UUID].json files
     */

    private File getPlayerFile(String directoryPath, String player_UUID) {
        File folder = new File(directoryPath);

        // Ensure the directory exists
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("[OneBlockPlugin] Directory created successfully at " + folder.getAbsolutePath());
            } else {
                System.out.println("[OneBlockPlugin] Failed to create directory at " + folder.getAbsolutePath());
                return null;
            }
        }

        // Define the target file based on the player's UUID
        File targetFile = new File(folder, player_UUID + ".json");

        // Check if the file exists
        if (targetFile.exists()) {
            System.out.println("[OneBlockPlugin] Player data file found: " + targetFile.getAbsolutePath());
            return targetFile;
        } else {
            System.out.println("[OneBlockPlugin] No data file found for player UUID: " + player_UUID);
            return null;
        }
    }


     /**
     * Extract the JSON content from a given file.
     *
     * @param file the JSON file to parse
     * @return the parsed JSONObject, or null if parsing fails
     */

    private PlayerData extractPlayerData(File file) {
        JSONParser parser = new JSONParser();
        PlayerData player_data = new PlayerData();

        try (FileReader reader = new FileReader(file)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            // --- Extract data to player_data

            // --- Extract UUID and set
            player_data.setUUID(fileManager.getFileNameWithoutExtension(file.getName()));

            // --- Extract Score
            int playerScore = ((Long) jsonObject.get("score")).intValue();

            // --- Set score
            player_data.setScore(playerScore);

            //--- Extract enabled lotNames
            List<String> lotsEnabledNames = (List<String>) jsonObject.get("enabled_lots");

            //If is a new player or doesn't have any enabled lots
            if(lotsEnabledNames.isEmpty()) {
                player_data.setEnabledLots(null);
            }

            // --- Gets the player lot data
            ArrayList<Lot> player_enabled_lots = returnJSONLotsArray(lotsEnabledNames, player_data.getUUID());

            if(player_enabled_lots == null) {
                System.out.println("[OneBlockPlugin] Error in player's data UUID " + player_data.getUUID() + ". Has encountered mismatched or bad written enabled lots.");
                player_data.setEnabledLots(new ArrayList<Lot>());
            }else {
                player_data.setEnabledLots(player_enabled_lots);
            }

            // --- Extract pool lots name
            List<String> lotsNames = (List<String>) jsonObject.get("lot_pool");

            // --- Gets the player lot data
            ArrayList<Lot> player_pool_lots = returnJSONLotsArray(lotsNames, player_data.getUUID());

            if (player_pool_lots != null) {
                player_data.setLotPool(player_pool_lots);
            } else {
                player_data.setLotPool(new ArrayList<Lot>());
            }
        } catch (IOException e) {
            System.err.println("Failed to read file: " + file.getName());
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Failed to parse JSON in file: " + file.getName());
            e.printStackTrace();
        }
        return player_data;
    }

    /**
    * Validate lot's name if exists
    *
    * @param listLots the List<String> containing lot names to validate
    * @return ArrayList<Lot> if all names in listLots are registered in the system, otherwise returns null
    */

    private ArrayList<Lot> returnJSONLotsArray(List<String> listLots, String playerID) {
        // Create a set of valid lot names for quick lookup
        Set<String> validLotNames = new HashSet<>();
        for (Lot lot : plugin.arrayLot) {
            validLotNames.add(lot.getLotName().toLowerCase().trim());
        }

        // Create a list to hold matched Lot objects
        ArrayList<Lot> matchedLots = new ArrayList<>();
        List<String> missingLots = new ArrayList<>();

        // Validate each name in listLots
        for (String lotName : listLots) {
            String normalizedLotName = lotName.toLowerCase().trim();
            if (validLotNames.contains(normalizedLotName)) {
                // Find the corresponding Lot object and add to the matched list
                for (Lot lot : plugin.arrayLot) {
                    if (lot.getLotName().toLowerCase().trim().equals(normalizedLotName)) {
                        matchedLots.add(lot);
                        break;
                    }
                }
            } else {
                // Add to missing lots if not found
                missingLots.add(lotName);
            }
        }

        // Log missing lots, if any
        if (!missingLots.isEmpty()) {
            System.out.println("[OneBlockPlugin Error] Missing lots: " + missingLots + " for player "+ playerID+ ". Check typo and if that lots exists.");
            return null;
        }

        return matchedLots;
    }


    /**
     * Load all player data stored in .json files from a directory and parse their contents.
     *
     * @param directoryPath the path to the directory
     */

    public ArrayList<PlayerData> loadPlayerData(String directoryPath) {
        List<File> filePlayer = getPlayersFiles(directoryPath);
        ArrayList<PlayerData> playerArray = new ArrayList<>();

        for (File file : filePlayer) {
            PlayerData playerdata = extractPlayerData(file);
            playerArray.add(playerdata);
        }
        return playerArray;
    }

    /**
     * Load a single player data stored in .json files from a directory and parse their contents.
     * @param player_UUID the name of the file without extension
     */

    public PlayerData loadSinglePlayerData(UUID player_UUID) {
        String directoryPath = "one_block_data/player_data/";
        File filePlayer = getPlayerFile(directoryPath,
                                         player_UUID.toString());

        if(filePlayer == null) { return null;}

        return extractPlayerData(filePlayer);
    }

}