package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class MultiSaveCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public MultiSaveCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Region region = plugin.regionManager.activeRegion;
        if (region == null) {
            sender.sendMessage(ChatColor.RED + "No plot is currently selected");
            return true;
        }

        int pieces = Integer.parseInt(args[0]);
        String filename;
        if (args.length == 1) {
            filename = region.name.replace(" ", "_");
        } else {
            filename = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        Component component = Component.text("Saving ", NamedTextColor.GRAY)
                .append(Component.text(region.name, NamedTextColor.DARK_AQUA))
                .append(Component.text(" to schematic...", NamedTextColor.GRAY));
        sender.sendMessage(component);
        plugin.regionManager.saveRegion(region, filename, pieces);
        return true;
    }
}
