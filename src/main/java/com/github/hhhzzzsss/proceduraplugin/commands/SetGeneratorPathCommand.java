package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGeneratorPathCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public SetGeneratorPathCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String path = String.join(" ", args);
        plugin.getConfig().set("generatorProgramsPath", path);
        plugin.saveConfig();
        Component component = Component.text("Saved generator programs path as ", NamedTextColor.GRAY)
                .append(Component.text(path, NamedTextColor.DARK_AQUA));
        sender.sendMessage(component);
        return true;
    }
}
