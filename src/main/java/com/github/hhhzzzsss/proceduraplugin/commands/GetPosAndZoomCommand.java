package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GetPosAndZoomCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public GetPosAndZoomCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0) {
            return false;
        }
        Component xComponent = Component.text(plugin.zoomManager.x.toString(), NamedTextColor.DARK_AQUA)
                .clickEvent(ClickEvent.copyToClipboard(plugin.zoomManager.x.toString()))
                .hoverEvent(HoverEvent.showText(Component.text("Click to copy x position", NamedTextColor.AQUA)));
        Component yComponent = Component.text(plugin.zoomManager.y.toString(), NamedTextColor.DARK_AQUA)
                .clickEvent(ClickEvent.copyToClipboard(plugin.zoomManager.y.toString()))
                .hoverEvent(HoverEvent.showText(Component.text("Click to copy y position", NamedTextColor.AQUA)));
        Component zComponent = Component.text(plugin.zoomManager.z.toString(), NamedTextColor.DARK_AQUA)
                .clickEvent(ClickEvent.copyToClipboard(plugin.zoomManager.z.toString()))
                .hoverEvent(HoverEvent.showText(Component.text("Click to copy z position", NamedTextColor.AQUA)));
        Component zoomComponent = Component.text(String.valueOf(plugin.zoomManager.zoom), NamedTextColor.DARK_AQUA)
                .clickEvent(ClickEvent.copyToClipboard(String.valueOf(plugin.zoomManager.zoom)))
                .hoverEvent(HoverEvent.showText(Component.text("Click to copy zoom", NamedTextColor.AQUA)));
        Component chatComponent = Component.text("Pos = ", NamedTextColor.GRAY)
                .append(xComponent)
                .append(Component.text(" ", NamedTextColor.DARK_AQUA))
                .append(yComponent)
                .append(Component.text(" ", NamedTextColor.DARK_AQUA))
                .append(zComponent)
                .append(Component.text(", Zoom = ", NamedTextColor.GRAY))
                .append(zoomComponent);
        sender.sendMessage(chatComponent);
        return true;
    }
}
