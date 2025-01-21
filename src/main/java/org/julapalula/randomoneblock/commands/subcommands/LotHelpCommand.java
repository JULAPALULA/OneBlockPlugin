package org.julapalula.randomoneblock.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.julapalula.randomoneblock.commands.LotSubCommands;

public class LotHelpCommand implements LotSubCommands {
    @Override
    public boolean execute(Player player, String[] args) {
        player.sendMessage(ChatColor.YELLOW +"[ROB] Available commands:");
        player.sendMessage("/lot - Opens overall menu.");
        player.sendMessage("/lot help - Display this help message.");
        player.sendMessage("/lot show <lot name> - Show details of a lot.");
        player.sendMessage("/lot list - List all lots and status.");
        player.sendMessage("/lot score - Display your current stats and score.");
        player.sendMessage("/lot buy <lot name> - Buy a specific lot.");
        player.sendMessage("/lot enable <lot name> - Enable a lot from your pool.");
        player.sendMessage("/lot disable <lot name> - Enable a lot from your pool.");
        return true;
    }


}
