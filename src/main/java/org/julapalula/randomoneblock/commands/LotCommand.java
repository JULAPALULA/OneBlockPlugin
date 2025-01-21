package org.julapalula.randomoneblock.commands;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.julapalula.randomoneblock.core.Lot;
import org.julapalula.randomoneblock.core.LotManager;
import org.julapalula.randomoneblock.core.ROBPlugin;
import org.julapalula.randomoneblock.playerinfo.PlayerData;
import org.julapalula.randomoneblock.playerinfo.PlayerManager;
import org.julapalula.randomoneblock.playerinfo.PlayerUnwrapper;

public class LotCommand{
    private ROBPlugin plugin;
    private final  Player player;
    private final PlayerUnwrapper playerUnwrapper = new PlayerUnwrapper(plugin);
    private final PlayerManager playerManager = new PlayerManager();

    public LotCommand(ROBPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        LotCommandExecution();
    }
    private void LotCommandExecution() {
        LotManager lm = new LotManager(plugin, player);
        PlayerData pld = playerUnwrapper.loadSinglePlayerData(player.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + "[Lot Overall Info] " + ChatColor.GREEN + player.getName() + " has " + ChatColor.DARK_RED + pld.getScore() + ChatColor.GREEN + " score");
        player.sendMessage(ChatColor.YELLOW + "Lot server list: ");

        for (Lot lot : plugin.arrayLot) {
            String lotName = lot.getLotName();
            boolean isBought = lm.isLotBought(lotName);
            boolean isEnabled = lm.isLotEnabled(lotName);

            ChatColor color;
            String status;
            String action;
            String command;

            if (isEnabled) {
                color = ChatColor.GREEN;
                status = "Enabled";
                action = "Click to disable";
                command = "/lot disable " + lotName;
            } else if (isBought) {
                color = ChatColor.RED;
                status = "Disabled";
                action = "Click to enable";
                command = "/lot enable " + lotName;
            } else {
                color = ChatColor.GRAY;
                status = "Not Bought";
                action = "Click to buy";
                command = "/lot buy " + lotName;
            }

            TextComponent message = new TextComponent(color + "- " + lotName + " (" + status + ")");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(action)));

            // Send the message to the player
            player.spigot().sendMessage(message);
        }
    }
}
