package org.julapalula.randomoneblock.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.julapalula.randomoneblock.commands.LotSubCommands;
import org.julapalula.randomoneblock.core.Lot;
import org.julapalula.randomoneblock.core.LotManager;
import org.julapalula.randomoneblock.core.ROBPlugin;

import java.util.List;

public class LotShowCommand implements LotSubCommands {

    private ROBPlugin plugin = null;

    public LotShowCommand(ROBPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        LotManager lm = new LotManager(plugin, player);

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "[ROB] Usage: /lot show <lot name>");
            return true;
        }

        String lotName = args[1];
        Lot lot = lm.getLotByName(lotName);
        player.sendMessage(lotName);

        if (lot == null) {
            player.sendMessage(ChatColor.RED + "[ROB] Lot '" + lotName + "' not found!");
            return true;
        }

        // Display details about the lot
        player.sendMessage(ChatColor.YELLOW + "[ROB] Details for lot: " + ChatColor.GREEN + lotName);
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
        float chancePerMaterial = (!lot_materials.isEmpty()) ? (float) 100 / lot_materials.size() : 0;

        for (Material material : lot_materials) {
            player.sendMessage(String.format(ChatColor.GOLD + "- %s (Chance: %s%.2f%%%s)", material.name(), ChatColor.DARK_RED, chancePerMaterial,ChatColor.GOLD));
        }
        return true;
    }

}
