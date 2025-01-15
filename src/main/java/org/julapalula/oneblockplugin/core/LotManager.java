package org.julapalula.oneblockplugin.core;

import org.bukkit.entity.Player;
import org.julapalula.oneblockplugin.playerinfo.PlayerData;
import org.julapalula.oneblockplugin.playerinfo.PlayerUnwrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LotManager {
    private Player player = null;
    private OneBlockPlugin plugin = null;
    private final PlayerUnwrapper player_unwrapper = new PlayerUnwrapper(plugin);

    public LotManager(OneBlockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    // Method to retrieve a lot object by its name
    public Lot getLotByName(String lotName) {
        for (Lot lot : plugin.arrayLot) {
            if (lot.getLotName().equalsIgnoreCase(lotName)) {
                return lot;
            }
        }
        return null; // Return null if no matching lot is found
    }

    // Method to check if a lot is enabled for a player
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

    // Method to check if a lot is enabled for a player
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

    // Method to check a list of lots for their enabled status
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
