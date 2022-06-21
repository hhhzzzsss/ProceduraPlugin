package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class SetCenterCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public SetCenterCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be an ingame player");
            return true;
        }
        Player player = ((Player) sender);
        Region region = plugin.regionManager.getContainingRegion(player.getLocation());
        
        if (region == null || region != plugin.regionManager.activeRegion) {
            sender.sendMessage("Must be standing in a selected region");
            return true;
        }
        
        double centerX = region.xpos + region.xdim / 2.0;
        double centerY = region.ypos + region.ydim / 2.0;
        double centerZ = region.zpos + region.zdim / 2.0;
        double offsetX = player.getLocation().getX() - centerX;
        double offsetY = player.getLocation().getY() - centerY;
        double offsetZ = player.getLocation().getZ() - centerZ;
        double scale = 2.0 * plugin.zoomManager.zoom / region.getMinDim();
        plugin.zoomManager.x = plugin.zoomManager.x.add(BigDecimal.valueOf(offsetX * scale));
        plugin.zoomManager.y = plugin.zoomManager.y.add(BigDecimal.valueOf(offsetY * scale));
        plugin.zoomManager.z = plugin.zoomManager.z.add(BigDecimal.valueOf(offsetZ * scale));

        Component component = Component.text("Set pos to ", NamedTextColor.GRAY)
                .append(Component.text(plugin.zoomManager.x + " " + plugin.zoomManager.y + " " + plugin.zoomManager.z, NamedTextColor.DARK_AQUA));
        sender.sendMessage(component);

        plugin.zoomManager.runProgram(sender);
        return true;
    }
}
