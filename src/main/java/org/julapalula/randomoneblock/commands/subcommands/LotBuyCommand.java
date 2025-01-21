package org.julapalula.randomoneblock.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.julapalula.randomoneblock.commands.LotSubCommands;
import org.julapalula.randomoneblock.core.Lot;
import org.julapalula.randomoneblock.core.LotManager;
import org.julapalula.randomoneblock.playerinfo.PlayerData;
import org.julapalula.randomoneblock.playerinfo.PlayerManager;
import org.julapalula.randomoneblock.playerinfo.PlayerUnwrapper;
import org.julapalula.randomoneblock.core.ROBPlugin;

import java.util.ArrayList;

public class LotBuyCommand implements LotSubCommands {

    private ROBPlugin plugin = null;
    private final PlayerUnwrapper playerUnwrapper = new PlayerUnwrapper(plugin);
    private final PlayerManager playerManager = new PlayerManager();

    public LotBuyCommand(ROBPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        //Check if the player provided a lot name
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "You must specify the name of the lot you want to buy.");
            return true;
        }

        String lotName = args[1];  // The lot name provided by the player
        LotManager lotManager = new LotManager(plugin, player);

        //Validate if the lot exists
        Lot lotToBuy = lotManager.getLotByName(lotName);

        if (lotToBuy == null) {
            player.sendMessage(ChatColor.RED + "The specified lot does not exist.");
            return true;
        }

        //Check if the player already owns the lot
        PlayerData playerData = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());

        ArrayList<Lot> playerLots = playerData.getLotPool();

        // If the player already owns the lot
        if (playerLots.contains(lotToBuy)) {
            player.sendMessage(ChatColor.RED + "You already own this lot.");
            return true;
        }

        // Can player buy lot?
        if (!canBuyIt(player, lotName)) {
            player.sendMessage(ChatColor.RED + "Don't have enough score to buy this lot. You need at least " + lotToBuy.getScore());
            return true;
        }

        // Update player data
        playerManager.addLotPool(player, lotToBuy.getLotName());

        //Provide feedback to the player
        player.sendMessage(ChatColor.YELLOW + "[ROB]"+ChatColor.GREEN + "You have successfully bought the lot: " + ChatColor.YELLOW + lotName);

        return true;
    }

    // Can buy that lot
    private boolean canBuyIt(Player player, String lotName) {
        LotManager lm = new LotManager(plugin, player);
        PlayerData pld = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());

        Lot lot = lm.getLotByName(lotName);

        int score = pld.getScore();
        int lot_score = lot.getScore();


        if(score >= lot_score) {
            playerManager.rewritePlayerScore(player, score-lot_score);
            return true;
        }else {
            return false;
        }
    }
}
