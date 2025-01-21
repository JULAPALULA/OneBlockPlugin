package org.julapalula.randomoneblock.listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.julapalula.randomoneblock.core.Lot;
import org.julapalula.randomoneblock.core.ROBPlugin;
import org.julapalula.randomoneblock.core.OneBlockTask;
import org.julapalula.randomoneblock.playerinfo.PlayerData;
import org.julapalula.randomoneblock.playerinfo.PlayerManager;
import org.julapalula.randomoneblock.playerinfo.PlayerUnwrapper;

import java.util.ArrayList;
import java.util.UUID;

public class ROBListeners implements Listener {
    private final PlayerManager player_manager = new PlayerManager();
    private ROBPlugin ROBPlugin = null;
    private final PlayerUnwrapper player_unwrapper = new PlayerUnwrapper(ROBPlugin);

    public ROBListeners(ROBPlugin ROBPlugin) {
        this.ROBPlugin = ROBPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createPlayerData(player);

        //Checks if player has enabled_lots
        if(hasPlayerEnabledLots(player)) {
            OneBlockTask obt = new OneBlockTask(ROBPlugin, player);
            ROBPlugin.setOneBlockTask(obt);
            //--- We start the task if has enabled lots when enter server
            obt.startTask();
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Handle player quit event
        if(hasPlayerEnabledLots(player)) {
            OneBlockTask obt = ROBPlugin.getOneBlockTask();
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
