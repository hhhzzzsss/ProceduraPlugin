package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeselectCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public DeselectCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.regionManager.activeRegion = null;
        plugin.bossbarHandler.updateAllBossbars();
        sender.sendMessage(ChatColor.GRAY + "Set active plot to null");
        return true;
    }
}
