package org.julapalula.oneblockplugin.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.julapalula.oneblockplugin.commands.LotSubCommands;
import org.julapalula.oneblockplugin.core.Lot;
import org.julapalula.oneblockplugin.core.LotManager;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;

import java.util.ArrayList;

public class LotListCommand implements LotSubCommands {

    private final OneBlockPlugin plugin;

    public LotListCommand(OneBlockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        LotManager lm = new LotManager(plugin, player);

        player.sendMessage(ChatColor.YELLOW + "[OneBlock] Lot server list: ");

        // Iterate through all lots in the server
        for (Lot lot : plugin.arrayLot) {
            String lotName = lot.getLotName();
            boolean isBought = lm.isLotBought(lotName);
            boolean isEnabled = lm.isLotEnabled(lotName);

            // Determine the lot status and assign colors
            ChatColor color;
            String status;
            if (isEnabled) {
                color = ChatColor.GREEN;
                status = "Enabled";
            } else if (isBought) {
                color = ChatColor.RED;
                status = "Disabled";
            } else {
                color = ChatColor.GRAY;
                status = "Not Bought";
            }

            // Send the formatted message to the player
            player.sendMessage(color + "- " + lotName + " (" + status + ")");
        }
        return true;
    }
}
