package org.julapalula.oneblockplugin.commands;
import org.bukkit.Material;
import org.julapalula.oneblockplugin.core.Lot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;
import org.julapalula.oneblockplugin.playerinfo.PlayerData;
import org.julapalula.oneblockplugin.playerinfo.PlayerManager;
import org.julapalula.oneblockplugin.playerinfo.PlayerUnwrapper;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class OneBlockCommands implements CommandExecutor {

    private static OneBlockPlugin plugin = null;
    private static PlayerUnwrapper playerUnwrapper =  new PlayerUnwrapper(plugin.arrayLot);
    private static PlayerManager playerManager =  new PlayerManager();

    public OneBlockCommands(OneBlockPlugin plugin) {
       this.plugin = plugin; // Populate with existing lots from the plugin.
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
            case "score":
                displayPlayerScore(player);
                return true;
            case "pool":
                displayPlayerPool(player);
                return true;
            case "buy":
                if (args.length != 2) {
                    player.sendMessage("[OneBlock] Usage: /lot buy <lot name>");
                    return true;
                }
                buyLot(player, args[1]);
                return true;
            case "enable":
                if (args.length != 1) {
                    List<String> lotNames = getAvaibableLot(); // Fetch all lot names
                    player.sendMessage("[OneBlock] Available lots: " + String.join(", ", lotNames));
                    return true;
                }
                String lotName = args[0].toLowerCase();
                playerManager.addLot(player,lotName);
                player.sendMessage("[OneBlock] Successfully added "+lotName + "to your pool of lots.");
                return true;
            default:
                sender.sendMessage("Ups that's not a command. Try too use /lot help for more info!");
                return true;
        }
        //return false;
    }

    private boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    private Lot getLotData(String lotName) {
        for (Lot lot : plugin.arrayLot) {
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
                "/lot disable <lot name> -> Disable lot\n"+
                "/lot score -> Shows my score\n"+
                "/lot pool -> Shows your obtained lots\n"+
                "/lot help -> Displays this list of commands";
        player.sendMessage(helpMessage);
    }

    // ╔════════════════╗
    // ║    lot score   ║
    // ╚════════════════╝

    private void displayPlayerScore(Player player) {
        PlayerData pld = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());
        player.sendMessage(player.getName() + " score: "+ pld.getScore());
    }

    // ╔════════════════╗
    // ║    lot pool    ║
    // ╚════════════════╝

    private void displayPlayerPool(Player player) {
        PlayerData pld = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());
        ArrayList<Lot> _arrayLot = pld.getEnabledLots();
        player.sendMessage("Current "+ player.getName() +" lot pool:\n");

        for(Lot _lot : _arrayLot) {
            player.sendMessage("* " + _lot.getLotName());
        }
    }

    // ╔════════════════╗
    // ║    lot buy     ║
    // ╚════════════════╝

    private void buyLot(Player player, String lotName) {
        PlayerManager plm = new PlayerManager();
            plm.addLot(player,lotName);
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
        for (Lot _lot :  plugin.arrayLot) {
            player.sendMessage(String.format("- %s (Chance: %d%%)", _lot.getLotName(), _lot.getScore()));
        }
    }

    // ╔══════════════════╗
    // ║    lot enable    ║
    // ╚══════════════════╝

   private List<String> getAvaibableLot() {
        List<String> lotStrList = new ArrayList<String>();
        for (Lot lot: plugin.arrayLot) {
            lotStrList.add(lot.getLotName());
        }
        return lotStrList;
   }
}
