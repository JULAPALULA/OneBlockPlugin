package org.julapalula.randomoneblock.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.julapalula.randomoneblock.commands.LotSubCommands;
import org.julapalula.randomoneblock.core.Lot;
import org.julapalula.randomoneblock.core.LotManager;
import org.julapalula.randomoneblock.core.ROBPlugin;
import org.julapalula.randomoneblock.core.OneBlockTask;
import org.julapalula.randomoneblock.playerinfo.PlayerData;
import org.julapalula.randomoneblock.playerinfo.PlayerManager;
import org.julapalula.randomoneblock.playerinfo.PlayerUnwrapper;

public class LotDisableCommand implements LotSubCommands {

    private ROBPlugin plugin = null;
    private final PlayerManager playerManager = new PlayerManager();
    private final PlayerUnwrapper playerUnwrapper = new PlayerUnwrapper(plugin);

    public LotDisableCommand(ROBPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        // Check if the player provided a lot name
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "You must specify the name of the lot you want to disable.");
            return true;
        }

        String lotName = args[1];  // The lot name provided by the player
        LotManager lotManager = new LotManager(plugin, player);

        //Find the lot by its name
        LotManager lm = new LotManager(plugin, player);
        Lot lotToEnable = lm.getLotByName(lotName);

        if (lotToEnable == null) {
            player.sendMessage(ChatColor.RED + "The specified lot does not exist.");
            return true;
        }

        // Check if the player already enabled it
        if(!lm.isLotEnabled(lotName)) {
            player.sendMessage(ChatColor.RED + "You already disable the lot!");
            return true;
        }

        //Disable the lot
        playerManager.removeLotEnabled(player, lotName);

        OneBlockTask obt = plugin.getOneBlockTask();
        obt.stopTask();

        //Check if has more disabled lots
        PlayerData pld = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());
        if(!pld.getEnabledLots().isEmpty()) {
            obt = new OneBlockTask(plugin, player);
            obt.startTask();
            plugin.setOneBlockTask(obt);
        }

        //Provide feedback to the player
        player.sendMessage(ChatColor.YELLOW + "[ROB]"+ChatColor.GREEN + "You have successfully disabled the lot: " + ChatColor.YELLOW + lotName);

        return true;
    }

}
