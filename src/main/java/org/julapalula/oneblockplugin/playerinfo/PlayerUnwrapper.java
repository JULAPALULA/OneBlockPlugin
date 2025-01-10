package org.julapalula.oneblockplugin.playerinfo;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.julapalula.oneblockplugin.core.Lot;
import org.julapalula.oneblockplugin.utils.OneBlockFileManagerUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PlayerUnwrapper {
    OneBlockFileManagerUtil fileManager = new OneBlockFileManagerUtil();
    JSONParser parser = new JSONParser();
    private static ArrayList<Lot> arrayLot = new ArrayList<Lot>();

    public PlayerUnwrapper(ArrayList<Lot> arrayLot ) {
        this.arrayLot = arrayLot;
    }

     /**
     * Get the list of [UUID].json files in the specified directory.
     *
     * @param directoryPath the path to the directory
     * @return a list of File objects representing [UUID].json files
     */

    private List<File> getPlayerFiles(String directoryPath) {
        File folder = new File(directoryPath);

        // Check if the folder exists
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("[OneBlockPlugin] player_data folder created with success in "+ folder.getAbsolutePath() +".");
            } else {
                System.out.println("[OneBlockPlugin] Failed to create player_data directory.");
                return new ArrayList<>();
            }
        }

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

            //Extract UUID
            player_data.setUUID(fileManager.getFileNameWithoutExtension(file.getName()));

            //Extract Score
            int playerScore = ((Long) jsonObject.get("score")).intValue();
            player_data.setScore(playerScore);

            //Extract lotNames
            List<String> lotsEnabledNames = (List<String>) jsonObject.get("enabled_lots");

            //If is a new player or doesn't have any lots
            if(lotsEnabledNames.isEmpty()) {
                player_data.setEnabledLots(null);
                return player_data;
            }

            //gets the player lot data
            ArrayList<Lot> player_enabled_lots = returnPlayerLots(lotsEnabledNames, player_data.getUUID());
            if(player_enabled_lots == null) {
                System.out.println("[OneBlockPlugin] Error in player's data UUID " + player_data.getUUID() + ". Has encountered mismatched or bad written lots.");
            }else {
                player_data.setEnabledLots(player_enabled_lots);
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

    private ArrayList<Lot> returnPlayerLots(List<String> listLots, String playerID) {

        // Create a set of valid lot names for quick lookup
        Set<String> validLotNames = new HashSet<>();
        for (Lot lot : arrayLot) {
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
                for (Lot lot : arrayLot) {
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
        List<File> filePlayer = getPlayerFiles(directoryPath);
        ArrayList<PlayerData> playerArray = new ArrayList<>();

        for (File file : filePlayer) {
            PlayerData playerdata = extractPlayerData(file);
            playerArray.add(playerdata);
        }
        return playerArray;
    }

    /**
     * Load a single player data stored in .json files from a directory and parse their contents.
     *
     * @param player_UUID the name of the file without extension
     */

    public PlayerData loadSinglePlayerData(String player_UUID) {
        String directoryPath = "one_block_data/player_data/"+player_UUID;
        List<File> filePlayer = getPlayerFiles(directoryPath);

        if(filePlayer.isEmpty()) { return null;}

        PlayerData playerdata = extractPlayerData(filePlayer.get(0));
        return playerdata;
    }
}
