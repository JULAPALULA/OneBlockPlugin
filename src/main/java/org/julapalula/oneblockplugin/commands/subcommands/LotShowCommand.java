package org.julapalula.oneblockplugin.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.julapalula.oneblockplugin.commands.LotSubCommands;
import org.julapalula.oneblockplugin.core.Lot;
import org.julapalula.oneblockplugin.core.LotManager;
import org.julapalula.oneblockplugin.core.OneBlockPlugin;

import java.util.List;

public class LotShowCommand implements LotSubCommands {

    private OneBlockPlugin plugin = null;

    public LotShowCommand(OneBlockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        LotManager lm = new LotManager(plugin, player);

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "[OneBlock] Usage: /lot show <lot name>");
            return true;
        }

        String lotName = args[1];
        Lot lot = lm.getLotByName(lotName);
        player.sendMessage(lotName);

        if (lot == null) {
            player.sendMessage(ChatColor.RED + "[OneBlock] Lot '" + lotName + "' not found!");
            return true;
        }

        // Display details about the lot
        player.sendMessage(ChatColor.YELLOW + "[OneBlock] Details for lot: " + ChatColor.GREEN + lotName);
        //Status
        boolean isLotBought =lm.isLotBought(lotName);
        if(isLotBought) {
            player.sendMessage(ChatColor.GOLD + "Lot status: " + ChatColor.GREEN + "Bought");
        } else {
            player.sendMessage(ChatColor.GOLD + "Lot status: " + ChatColor.RED + "Not Acquired");
        }
        //Score
        if(isLotBought) {
            player.sendMessage(ChatColor.GOLD + "Lot original score: " + ChatColor.GREEN + lot.getScore());
        } else {
            player.sendMessage(ChatColor.GOLD + "Lot score: " + ChatColor.RED + lot.getScore());
        }

        boolean isLotEnabled =lm.isLotEnabled(lotName);
        player.sendMessage(ChatColor.GOLD + "Enabled: " + (isLotEnabled ? ChatColor.GREEN+"Yes" : ChatColor.RED+"No"));

        player.sendMessage(ChatColor.GOLD + "This lot contains: ");
        List<Material> lot_materials = lot.getMaterials();
        int chancePerMaterial = (!lot_materials.isEmpty()) ? 100 / lot_materials.size() : 0;

        for (Material material : lot_materials) {
            player.sendMessage(String.format(ChatColor.GOLD + "- %s (Chance: %s%d%%%s)", material.name(), ChatColor.DARK_RED, chancePerMaterial,ChatColor.GOLD));
        }
        return true;
    }

}
