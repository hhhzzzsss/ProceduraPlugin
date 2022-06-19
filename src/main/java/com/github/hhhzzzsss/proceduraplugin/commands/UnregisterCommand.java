package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnregisterCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public UnregisterCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be an ingame player");
            return true;
        }

        Region region = plugin.regionManager.activeRegion;

        try {
            plugin.regionManager.deleteRegion(region);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return true;
        }

        Component component = Component.text("Unregistered ", NamedTextColor.GRAY)
                .append(Component.text(region.name, NamedTextColor.DARK_AQUA));
        sender.sendMessage(component);
        return true;
    }
}
