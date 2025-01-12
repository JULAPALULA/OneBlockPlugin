package org.julapalula.oneblockplugin.listeners;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;
import org.julapalula.oneblockplugin.playerinfo.PlayerData;
import org.julapalula.oneblockplugin.playerinfo.PlayerManager;
import org.julapalula.oneblockplugin.playerinfo.PlayerUnwrapper;

import java.util.UUID;

public class OneBlockListeners implements Listener {
    private final PlayerManager player_manager = new PlayerManager();
    private final PlayerUnwrapper player_unwrapper = new PlayerUnwrapper();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createPlayerData(player);
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
        player.sendMessage(ChatColor.GREEN + "You placed a block! Your score is now: "
                + ChatColor.YELLOW + current_player_score);
    }

    private void createPlayerData(Player player) {
        //It's the first time entering the server?
        if(!player_manager.doesPlayerDataExist(player)) {
            player_manager.createPlayerData(player); //Creates player data
            return;
        }
    }


}
