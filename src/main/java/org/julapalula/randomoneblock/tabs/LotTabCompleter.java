package org.julapalula.randomoneblock.tabs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class manages on tab command in game actually suggests the commands.
 **/
public class LotTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Suggest subcommands for the first argument
            completions.addAll(Arrays.asList("help", "show", "list", "score", "buy", "enable", "disable"));
        } else if (args.length == 2) {
            // Suggest additional arguments based on the first subcommand
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("enable") || subCommand.equals("disable") || subCommand.equals("buy")) {
                completions.add("<lot_name>"); // Replace this with actual lot names if available
            }
        }

        return completions;
    }
}
