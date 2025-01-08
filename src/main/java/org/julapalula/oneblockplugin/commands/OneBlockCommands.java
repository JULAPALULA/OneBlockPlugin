package org.julapalula.oneblockplugin.commands;
import org.julapalula.oneblockplugin.core.Lot;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OneBlockCommands implements CommandExecutor {

    private static final ArrayList<Lot> lodLots = new ArrayList<>();

    public OneBlockCommands(ArrayList<Lot> lodLots) {
        OneBlockCommands.lodLots.clear();  // Clear existing contents
        OneBlockCommands.lodLots.addAll(lodLots);  // Add all elements from the input
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        //Checks if it's a plyer the sender
        if(!isPlayer(sender)) {
            sender.sendMessage("Only players can use this command!");
            return false;
        }
        Player player = (Player) sender;

        switch(command.getName().toLowerCase()) {
            case "selectlot":
                if (args.length != 1) {
                    List<String> lotNames = getLotNames(); // Fetch all lot names
                    player.sendMessage("OneBlock: Available lots: " + String.join(", ", lotNames));
                    return true;
                }
                String lotName = args[0].toLowerCase();

//                if (MaterialLots.getRandomMaterialFromBundel(lotName) == null) {
//                    //player.sendMessage("Invalid lot! Available lots: " + String.join(", ", MaterialLots.getLotNames()));
//                    return true;
//                }
                break;
            default:
                sender.sendMessage("Ups that's not a command. Try too use /lot help for help!");
        }

        return false;
    }

    private boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public static List<String> getLotNames() {
        ArrayList<String> lotNames = new ArrayList<>();
        for (Lot lot : lodLots) {
            lotNames.add(lot.getLotName());
        }
        return lotNames;
    }
}
