package org.julapalula.randomoneblock.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.julapalula.randomoneblock.commands.LotSubCommands;
import org.julapalula.randomoneblock.core.Lot;
import org.julapalula.randomoneblock.core.LotManager;
import org.julapalula.randomoneblock.core.ROBPlugin;
import org.julapalula.randomoneblock.playerinfo.PlayerData;
import org.julapalula.randomoneblock.playerinfo.PlayerUnwrapper;

import java.util.ArrayList;

public class LotScoreCommand implements LotSubCommands {
    private ROBPlugin plugin = null;
    private final PlayerUnwrapper player_unwrapper = new PlayerUnwrapper(plugin);

    public LotScoreCommand(ROBPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        LotManager lm = new LotManager(plugin, player);
        PlayerData pld = player_unwrapper.loadSinglePlayerData(player.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + "[ROB] Stats for player: " + ChatColor.GREEN + player.getName());
        player.sendMessage(ChatColor.GOLD + "Total score: " + ChatColor.GREEN + pld.getScore());

        // Retrieve enabled lots
        ArrayList<Lot> enabledLots = pld.getEnabledLots();

        if (enabledLots.isEmpty()) {
            // No enabled lots
            player.sendMessage(ChatColor.GOLD + "Enabled lots: " + ChatColor.RED + "NONE");
        } else {
            // Concatenate lot names
            String strEnabledLots = enabledLots.stream()
                    .map(Lot::getLotName)
                    .reduce((first, second) -> first + ", " + second)
                    .orElse("");

            // Determine singular/plural wording
            String lotLabel = enabledLots.size() == 1 ? "Enabled lot: " : "Enabled lots: ";
            player.sendMessage(ChatColor.GOLD + lotLabel + ChatColor.GREEN + strEnabledLots);

        }

        // Retrieve pool lots
        ArrayList<Lot> poolLots = pld.getLotPool();

        if (poolLots.isEmpty()) {
            // No enabled lots
            player.sendMessage(ChatColor.GOLD + "Bought lots: " + ChatColor.RED + "NONE");
        } else {
            // Concatenate lot names
            String strPoolLots = poolLots.stream()
                    .map(Lot::getLotName)
                    .reduce((first, second) -> first + ", " + second)
                    .orElse("");

            // Determine singular/plural wording
            String lotLabel = poolLots.size() == 1 ? "Bought lot: " : "Bought lots: ";
            player.sendMessage(ChatColor.GOLD + lotLabel + ChatColor.GREEN + strPoolLots);

        }
        return true;
    }
}
