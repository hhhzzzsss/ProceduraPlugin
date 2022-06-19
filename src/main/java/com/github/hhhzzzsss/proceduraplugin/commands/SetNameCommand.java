package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class SetNameCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public SetNameCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Region region = plugin.regionManager.activeRegion;
        if (region == null) {
            sender.sendMessage(ChatColor.RED + "No region is currently selected");
            return true;
        }

        try {
            plugin.regionManager.setRegionName(region, String.join(" ", args));
        } catch (IOException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return true;
        }

        Component component = Component.text("Set region name to ", NamedTextColor.GRAY)
                .append(Component.text(region.name, NamedTextColor.DARK_AQUA));
        sender.sendMessage(component);
        return true;
    }
}
