package org.julapalula.oneblockplugin.commands;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.checkerframework.checker.units.qual.A;
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
                if (args.length > 1 && args[1].equals("enabled")) {
                    // Execute when the command is `/lot list enabled`
                    playerEnabledList(player);
                }else {
                    lotList(player);
                }
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
                player.sendMessage("[OneBlock] Successfully bought "+ args[1] + " lot.");
                return true;
            case "enable":
                if (args.length != 2) {
                    player.sendMessage("[OneBlock] Usage: /lot enabled <lot name>, use /lot pool to see your available lots");
                    return true;
                }
                String lotName = args[1].toLowerCase();
                lotEnable(player, lotName);
                return true;
            case "disable":
                if (args.length != 2) {
                    player.sendMessage("[OneBlock] Usage: /lot disable <lot name>, use /lot list enabled to see your available lots");
                    return true;
                }
                String lotEnableName = args[1].toLowerCase();
                lotDisable(player, lotEnableName);
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
                "/lot buy <lot name> -> Buy a lot\n"+
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
        player.sendMessage(ChatColor.GREEN + "Current : " + player.getName() + " score:" + ChatColor.YELLOW +  pld.getScore());
    }

    // ╔════════════════╗
    // ║    lot pool    ║
    // ╚════════════════╝

    private void displayPlayerPool(Player player) {
        PlayerData pld = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());
        ArrayList<Lot> _arrayLot = pld.getLotPool();

        if(_arrayLot.isEmpty()) {
            player.sendMessage("No registered lots for this player"+ player.getName() +" lot pool:\n");
        }

        player.sendMessage("Current "+ player.getName() +" lot pool:\n");

        for(Lot _lot : _arrayLot) {
            player.sendMessage("* " + _lot.getLotName());
        }
    }

    // ╔════════════════╗
    // ║    lot buy     ║
    // ╚════════════════╝

    private boolean isLotNameInServer(String lotName) {
        ArrayList<Lot> server_lot_pool = plugin.arrayLot;
        boolean isLotNameReal = false;
        for(Lot _lot: server_lot_pool) {
            if(_lot.getLotName().equalsIgnoreCase(lotName)) {
                isLotNameReal = true;
                break;
            }
        }
        return isLotNameReal;
    }
    private boolean isLotAlreadyBought(Player player, String lotName) {
        PlayerData pld = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());
        boolean isLotBought = false;

        ArrayList<Lot> player_lot_pool = pld.getLotPool();
        for(Lot _lot: player_lot_pool) {
            if(_lot.getLotName().equalsIgnoreCase(lotName)) {
                isLotBought = true;
                break;
            }
        }
        return isLotBought;
    }
    private boolean canBuyIt(Player player, String lotName) {
        ArrayList<Lot> server_lot_pool = plugin.arrayLot;
        int lot_score = 0;
        for(Lot _lot: server_lot_pool) {
            if(_lot.getLotName().equalsIgnoreCase(lotName)) {
                lot_score = _lot.getScore();
                break;
            }
        }

        PlayerData pld = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());
        int score = pld.getScore();

        if(score >= lot_score) {
            playerManager.rewritePlayerScore(player, score-lot_score);
            return true;
        }else {
            return false;
        }
    }
    private void buyLot(Player player, String lotName) {
         /*
         * 1. Exists that lot in server?
         * 1. Check if that lot is already bought
         * 2. Can afford it?
         */
        if(!isLotNameInServer(lotName)) {
            player.sendMessage("[OneBlock] No matching lot name with any on server list try /lot list to ensure if exists.");
            return;
        }
        if(isLotAlreadyBought(player, lotName)) {
            player.sendMessage("[OneBlock] Lot "+ lotName+ " is already bought!");
            return;
        }
        if(!canBuyIt(player, lotName)) {
            player.sendMessage("[OneBlock] Can't buy "+ lotName+ ".");
            return;
        }
        playerManager.addLotPool(player,lotName);
        player.sendMessage("[OneBlock] Successfully bought "+ lotName+ "!");

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
            player.sendMessage(String.format("- %s (Score: %d%%)", _lot.getLotName(), _lot.getScore()));
        }
    }

    private void playerEnabledList(Player player){
        PlayerData pld = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());
        ArrayList<Lot> enabled_lot_array = pld.getEnabledLots();
        if(enabled_lot_array.isEmpty()) {
            player.sendMessage("No enabled lots are active!");
            return;
        }
        player.sendMessage("Current enabled lot list:");

        for(Lot _lot: enabled_lot_array) {
            player.sendMessage(String.format("- %s",_lot.getLotName()));
        }
    }

    // ╔══════════════════╗
    // ║    lot enable    ║
    // ╚══════════════════╝

   private void lotEnable(Player player, String lotName) {
       /*
        * 1. Checks if that lot name is in our pool
        * 2. Checks if that lot name is already enabled
        * 3. Enable it!
        */
        if(!checkLotInPlayerPool(player, lotName)) {
            player.sendMessage("[OneBlock] Lot "+ lotName+ " is not in your pool!");
            return;
        }
        if(checkLotIsAlreadyEnabled(player, lotName)) {
            player.sendMessage("[OneBlock] Lot "+ lotName+ " is already enabled!");
            return;
        }
        playerManager.addLotEnabled(player, lotName);
        player.sendMessage("[OneBlock] Enabled lot: "+ lotName);

   }
   private boolean checkLotInPlayerPool(Player player,String lotName) {
        PlayerData pd = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());
       ArrayList<Lot> player_lot_pool = pd.getLotPool();
       boolean isLotInPool = false;

       for(Lot _lot: player_lot_pool) {
           if(_lot.getLotName().equalsIgnoreCase(lotName)) {
               isLotInPool = true;
               break;
           }
       }
       return isLotInPool;
   }
   private boolean checkLotIsAlreadyEnabled(Player player,String lotName) {
        PlayerData pd = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());
        ArrayList<Lot> player_lot_enabled = pd.getEnabledLots();
        boolean isLotEnabled = false;

        for(Lot _lot: player_lot_enabled) {
            if(_lot.getLotName().equalsIgnoreCase(lotName)) {
                isLotEnabled = true;
                break;
            }
        }
        return isLotEnabled;
    }

    // ╔══════════════════╗
    // ║    lot disable   ║
    // ╚══════════════════╝

    private void lotDisable(Player player, String lotName) {
         /*
         * 1. Checks if that lot name is in our enabled pool
         * 2. Disable it!
         */

        if(!checkLotIsAlreadyEnabled(player, lotName)) {
            player.sendMessage("[OneBlock] Lot "+ lotName+ " is already disabled.");
            return;
        }

        playerManager.removeLotEnabled(player, lotName);
        player.sendMessage("[OneBlock] Disabled lot: "+ lotName);

    }
}
