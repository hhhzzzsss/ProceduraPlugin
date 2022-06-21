package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;

public class InitPosAndZoomCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public InitPosAndZoomCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 4) {
            return false;
        }
        plugin.zoomManager.x = new BigDecimal(args[0]);
        plugin.zoomManager.y = new BigDecimal(args[1]);
        plugin.zoomManager.z = new BigDecimal(args[2]);
        plugin.zoomManager.zoom = Double.parseDouble(args[3]);
        plugin.zoomManager.recalculateScale();
        Component component = Component.text("Set pos to ", NamedTextColor.GRAY)
                .append(Component.text(plugin.zoomManager.x + " " + plugin.zoomManager.y + " " + plugin.zoomManager.z, NamedTextColor.DARK_AQUA))
                .append(Component.text(" and zoom to ", NamedTextColor.GRAY))
                .append(Component.text(plugin.zoomManager.zoom, NamedTextColor.DARK_AQUA));
        sender.sendMessage(component);

        plugin.zoomManager.runProgram(sender);
        return true;
    }
}
