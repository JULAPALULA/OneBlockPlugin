package org.julapalula.randomoneblock.commands;
import org.bukkit.ChatColor;
import org.julapalula.randomoneblock.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.julapalula.randomoneblock.core.ROBPlugin;
import java.util.HashMap;
import java.util.Map;

public class ROBCommands implements CommandExecutor {

    private ROBPlugin plugin;
    private final Map<String, LotSubCommands> subCommandMap = new HashMap<>();

    public ROBCommands(ROBPlugin plugin) {
        this.plugin = plugin;
        registerSubCommands();
    }

    private void registerSubCommands() {
        subCommandMap.put("help", new LotHelpCommand());
        subCommandMap.put("show", new LotShowCommand(plugin));
        subCommandMap.put("list", new LotListCommand(plugin));
        subCommandMap.put("score", new LotScoreCommand(plugin));
        subCommandMap.put("buy", new LotBuyCommand(plugin));
        subCommandMap.put("enable", new LotEnableCommand(plugin));
        subCommandMap.put("disable", new LotDisableCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!isPlayer(sender)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            LotCommand mainLotCommand = new LotCommand(plugin, player);
            return true;
        }

        LotSubCommands subCommand = subCommandMap.get(args[0].toLowerCase());

        if (subCommand == null) {
            player.sendMessage(ChatColor.RED + "[ROB] Unknown subcommand! Type /lot help for available commands.");
            return true;
        }

        return subCommand.execute(player, args);
    }

    private boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }
}

