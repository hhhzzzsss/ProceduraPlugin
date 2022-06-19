package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public SelectCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);

            Region region = plugin.regionManager.getContainingRegion(player.getLocation());

            if (region == null) {
                player.sendMessage(ChatColor.GRAY + "You are not standing in a valid region");
                return true;
            }

            plugin.regionManager.activeRegion = region;
            plugin.bossbarHandler.updateAllBossbars();
            player.sendMessage(ChatColor.GRAY + "Set selected plot to " + ChatColor.DARK_AQUA + region.name);
            return true;
        } else {
            sender.sendMessage("Must be an ingame player");
            return true;
        }
    }
}
