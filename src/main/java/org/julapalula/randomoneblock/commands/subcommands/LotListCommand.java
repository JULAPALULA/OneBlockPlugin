package org.julapalula.randomoneblock.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.julapalula.randomoneblock.commands.LotSubCommands;
import org.julapalula.randomoneblock.core.Lot;
import org.julapalula.randomoneblock.core.LotManager;
import org.julapalula.randomoneblock.core.ROBPlugin;

public class LotListCommand implements LotSubCommands {

    private final ROBPlugin plugin;

    public LotListCommand(ROBPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        LotManager lm = new LotManager(plugin, player);

        player.sendMessage(ChatColor.YELLOW + "[ROB] Lot server list: ");

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
