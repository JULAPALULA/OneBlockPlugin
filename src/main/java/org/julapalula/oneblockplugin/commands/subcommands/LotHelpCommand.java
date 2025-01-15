package org.julapalula.oneblockplugin.commands.subcommands;

import org.bukkit.entity.Player;
import org.julapalula.oneblockplugin.commands.LotSubCommands;
import org.julapalula.oneblockplugin.core.OneBlockTask;

public class LotHelpCommand implements LotSubCommands {
    @Override
    public boolean execute(Player player, String[] args) {
        player.sendMessage("[OneBlock] Available commands:");
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
