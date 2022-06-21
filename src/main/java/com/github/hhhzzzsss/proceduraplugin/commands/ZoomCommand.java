package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.github.hhhzzzsss.proceduraplugin.region.ZoomManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ZoomCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public ZoomCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }
        double factor = Double.parseDouble(args[0]);
        plugin.zoomManager.zoom *= factor;
        plugin.zoomManager.recalculateScale();
//        Component component = Component.text("Multiplied zoom by ", NamedTextColor.GRAY)
//                .append(Component.text(factor, NamedTextColor.DARK_AQUA));
//        sender.sendMessage(component);
        Component component2 = Component.text("Zoom is now ", NamedTextColor.GRAY)
                .append(Component.text(plugin.zoomManager.zoom, NamedTextColor.DARK_AQUA));
        sender.sendMessage(component2);

        plugin.zoomManager.runProgram(sender);
        return true;
    }
}
