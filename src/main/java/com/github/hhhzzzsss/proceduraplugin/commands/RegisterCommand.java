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
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public RegisterCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be an ingame player");
            return true;
        }

        int xdim = 256;
        int ydim = 256;
        int zdim = 256;
        int xpos = ((Player) sender).getLocation().getBlockX();
        int ypos = 0;
        int zpos = ((Player) sender).getLocation().getBlockZ();
        if (args.length == 0) {
            // Nothing, use defaults
        } else if (args.length == 3) {
            try {
                xdim = Integer.parseInt(args[0]);
                ydim = Integer.parseInt(args[1]);
                zdim = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Arguments must be valid integers");
                return true;
            }
            if (ydim <= 256-50) {
                ypos = 50;
            } else if (ydim <= 256) {
                ypos = 0;
            } else {
                ypos = -64;
            }
        } else if (args.length == 4) {
            try {
                xdim = Integer.parseInt(args[0]);
                ydim = Integer.parseInt(args[1]);
                zdim = Integer.parseInt(args[2]);
                ypos = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Arguments must be valid integers");
                return true;
            }
        } else {
            return false;
        }

        try {
            plugin.regionManager.createRegion(xpos, ypos, zpos, xdim, ydim, zdim);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return true;
        }

        Component component = Component.text("Registered new region at ", NamedTextColor.GRAY)
                .append(Component.text(xpos + " " + ypos + " " + zpos, NamedTextColor.DARK_AQUA))
                .append(Component.text(" with dimensions ", NamedTextColor.GRAY))
                .append(Component.text(xdim + " " + ydim + " " + zdim, NamedTextColor.DARK_AQUA));
        sender.sendMessage(component);
        return true;
    }
}
