package org.julapalula.oneblockplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.julapalula.oneblockplugin.core.OneBlockTask;
import org.julapalula.oneblockplugin.lots.MaterialLots;

public class OneBlockCommands implements CommandExecutor {

    private final OneBlockTask taskManager;

    public OneBlockCommands(OneBlockTask taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //Check if it's a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        // --- selectlot
        if (command.getName().equalsIgnoreCase("selectlot")) {
            if (args.length != 1) {
                player.sendMessage("OneBlock: Available lots: " + String.join(", ", MaterialLots.getLoteNames()));
                return true;
            }
            String lotName = args[0].toLowerCase();

            if (MaterialLots.getRandomMaterialFromLote(lotName) == null) {
                player.sendMessage("Invalid lot! Available lote: " + String.join(", ", MaterialLots.getLoteNames()));
                return true;
            }

            taskManager.startTask(player, lotName,
                    () -> player.sendMessage("Error: The lot '" + lotName + "' is no longer valid!"),
                    () -> player.sendMessage("You received a random item from the '" + lotName + "' lot!")
            );

            player.sendMessage("You have selected the lot: " + lotName);
            return true;
        }

        // --- currentlot
        if (command.getName().equalsIgnoreCase("currentlot")) {
            String selectedLot = taskManager.getSelectedLot(player);
            player.sendMessage("You currently have selected the lot: " + selectedLot);
            return true;
        }
        return true;
    }
}
