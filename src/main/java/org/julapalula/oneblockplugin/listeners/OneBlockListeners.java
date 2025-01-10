package org.julapalula.oneblockplugin.listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;
import org.julapalula.oneblockplugin.playerinfo.PlayerManager;

public class OneBlockListeners implements Listener {
    private final OneBlockPlugin plugin;
    private final PlayerManager player_manager = new PlayerManager();

    public OneBlockListeners(OneBlockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createOrReadPlayerData(player);
    }

    private void createOrReadPlayerData(Player player) {
        if(!player_manager.doesPlayerDataExist(player)) {
            player.sendMessage("Creating player json file...");
            player_manager.createPlayerData(player);
            return;
        }

        player.sendMessage("No need to create json file yeah!");
    }
}
