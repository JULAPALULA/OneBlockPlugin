package org.julapalula.oneblockplugin.commands;
import org.bukkit.Material;
import org.julapalula.oneblockplugin.core.Lot;

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
        //Checks if it's a player the sender
        if(!isPlayer(sender)) {
            sender.sendMessage("Only players can use this command!");
            return false;
        }

        Player player = (Player) sender;

        // Ensure the base command is "lot"
        if (!command.getName().equalsIgnoreCase("lot")) {
            return false;
        }

        if (args.length == 0) {
           player.sendMessage("Not implemented yet!"); //TODO: Here should be the cool command
           return true;
        }

        // Handle subcommands
        switch(args[0].toLowerCase()) {
            case "help":
                displayHelp(player);
                return true;
            case "show":
                if (args.length != 2) {
                    player.sendMessage("[OneBlock] Usage: /lot show <lot name>");
                    return true;
                }
                lotShow(player, args[1]);
                return true;
            case "list":
                lotList(player);
                return true;
            case "enable":
                if (args.length != 1) {
                    List<String> lotNames = getLotNames(); // Fetch all lot names
                    player.sendMessage("[OneBlock] Available lots: " + String.join(", ", lotNames));
                    return true;
                }
                String lotName = args[0].toLowerCase();
                break;

            default:
                sender.sendMessage("Ups that's not a command. Try too use /lot help for more info!");
                return true;
        }
        return false;
    }

    private boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    private Lot getLotData(String lotName) {
        for (Lot lot : lodLots) {
            if (lot.getLotName().equalsIgnoreCase(lotName)) { // Compare ignoring case
                return lot; // Return the matching Lot
            }
        }
        return null;
    }

    // ╔════════════════╗
    // ║    lot help    ║
    // ╚════════════════╝

    private void displayHelp(Player player) {
        String helpMessage = "[OneBlock] Available Commands:\n" +
                "/lot -> Displays user friendly interactive menu!\n" +
                "/lot show <lot name> -> Displays lot material info and chance\n" +
                "/lot list -> Displays lot names and cost\n" +
                "/lot enable <lot name> -> Enable lot\n" +
                "/lot disable <lot name> -> Disable lot\n" +
                "/lot help -> Displays this list of commands";
        player.sendMessage(helpMessage);
    }

    // ╔════════════════╗
    // ║    lot show    ║
    // ╚════════════════╝

    private void lotShow(Player player, String lotName) {
       Lot _lot = getLotData(lotName);
       if (_lot == null) {
           player.sendMessage("[OneBlock] No matching lot name with any.");
           return;
       }
        player.sendMessage("Lot name: "+ _lot.getLotName() + " | Lot score: "+ _lot.getScore());
        player.sendMessage("Materials in this lot with chance to get them:");

        List<Material> lot_materials = _lot.getMaterials();
        int chancePerMaterial = (!lot_materials.isEmpty()) ? 100 / lot_materials.size() : 0; //calculate the chance to get the item

        for (Material material : lot_materials) {
            player.sendMessage(String.format("- %s (Chance: %d%%)", material.name(), chancePerMaterial));
        }
    }

    // ╔════════════════╗
    // ║    lot list    ║
    // ╚════════════════╝

    private void lotList(Player player) {
        for (Lot _lot : lodLots) {
            player.sendMessage(String.format("- %s (Chance: %d%%)", _lot.getLotName(), _lot.getScore()));
        }
    }

    // ╔══════════════════╗
    // ║    lot enable    ║
    // ╚══════════════════╝

    public static ArrayList<String> getLotNames() {
        //TODO:
        ArrayList<String> lotNames = new ArrayList<>();
        for (Lot lot : lodLots) {
            lotNames.add(lot.getLotName());
        }
        return lotNames;
    }
}
