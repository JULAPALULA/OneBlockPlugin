package org.julapalula.oneblockplugin.listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;
import org.julapalula.oneblockplugin.playerinfo.PlayerData;
import org.julapalula.oneblockplugin.playerinfo.PlayerManager;
import org.julapalula.oneblockplugin.playerinfo.PlayerUnwrapper;

public class OneBlockListeners implements Listener {
    private final PlayerManager player_manager = new PlayerManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createPlayerData(player);
    }

    private void createPlayerData(Player player) {
        //It's the first time entering the server?
        if(!player_manager.doesPlayerDataExist(player)) {
            player_manager.createPlayerData(player); //Creates player data
            return;
        }
    }
}
