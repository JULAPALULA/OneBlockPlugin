package org.julapalula.oneblockplugin.listeners;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.julapalula.oneblockplugin.core.Lot;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;
import org.julapalula.oneblockplugin.core.OneBlockTask;
import org.julapalula.oneblockplugin.playerinfo.PlayerData;
import org.julapalula.oneblockplugin.playerinfo.PlayerManager;
import org.julapalula.oneblockplugin.playerinfo.PlayerUnwrapper;

import java.util.ArrayList;
import java.util.UUID;

public class OneBlockListeners implements Listener {
    private final PlayerManager player_manager = new PlayerManager();
    private final PlayerUnwrapper player_unwrapper = new PlayerUnwrapper();
    private OneBlockPlugin oneBlockPlugin = null;

    public OneBlockListeners(OneBlockPlugin oneBlockPlugin) {
        this.oneBlockPlugin = oneBlockPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createPlayerData(player);

        //Checks if player has enabled_lots
        if(hasPlayerEnabledLots(player)) {
            OneBlockTask obt = new OneBlockTask(oneBlockPlugin, player);
            oneBlockPlugin.setOneBlockTask(obt);
            //--- We start the task if has enabled lots when enter server
            obt.startTask();
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Handle player quit event
        if(hasPlayerEnabledLots(player)) {
            OneBlockTask obt = oneBlockPlugin.getOneBlockTask();
            obt.stopTask();
        }
    }

    @EventHandler
    public void onPlayer(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Handle player quit event
        if(hasPlayerEnabledLots(player)) {
            OneBlockTask obt = new OneBlockTask(oneBlockPlugin, player);
            obt.stopTask();
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Load player data
        PlayerData pld = player_unwrapper.loadSinglePlayerData(playerId);

        // Increment score
        int current_player_score = pld.getScore();
        current_player_score++;
        player_manager.rewritePlayerScore(player, current_player_score);
        // Notify the player with color

    }

    private void createPlayerData(Player player) {
        //It's the first time entering the server?
        if(!player_manager.doesPlayerDataExist(player)) {
            player_manager.createPlayerData(player); //Creates player data
            return;
        }
    }
    private boolean hasPlayerEnabledLots(Player player) {
        // Load the player's data
        PlayerData pld = player_unwrapper.loadSinglePlayerData(player.getUniqueId());

        // Check if the enabled lots list is not null and contains elements
        ArrayList<Lot> enabledLots = pld.getEnabledLots();
        return enabledLots != null && !enabledLots.isEmpty();
    }
}
