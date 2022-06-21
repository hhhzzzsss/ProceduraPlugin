package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetProgramCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public SetProgramCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.zoomManager.program = args[0];
        Component component = Component.text("Set program to ", NamedTextColor.GRAY)
                .append(Component.text(plugin.zoomManager.program, NamedTextColor.DARK_AQUA));
        sender.sendMessage(component);
        return true;
    }
}
