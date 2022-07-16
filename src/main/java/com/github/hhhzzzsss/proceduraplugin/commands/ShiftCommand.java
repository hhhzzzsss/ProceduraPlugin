package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.math.BigDecimal;

public class ShiftCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public ShiftCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be an ingame player");
            return true;
        }
        if (args.length != 1) {
            return false;
        }

        Player player = ((Player) sender);
        double distance = Double.parseDouble(args[0]);
        Region region = plugin.regionManager.activeRegion;
        if (region == null) {
            sender.sendMessage("Must have a region selected");
            return true;
        }

        int bigDecimalScale = (int) Math.ceil(-Math.log10(plugin.zoomManager.zoom)) + 10;
        Vector offset = player.getLocation().getDirection().multiply(-distance);
        double scale = 2.0 * plugin.zoomManager.zoom / region.getMinDim();
        plugin.zoomManager.x = plugin.zoomManager.x.add(BigDecimal.valueOf(offset.getX() * scale).setScale(bigDecimalScale));
        plugin.zoomManager.y = plugin.zoomManager.y.add(BigDecimal.valueOf(offset.getX() * scale).setScale(bigDecimalScale));
        plugin.zoomManager.z = plugin.zoomManager.z.add(BigDecimal.valueOf(offset.getZ() * scale).setScale(bigDecimalScale));

        plugin.zoomManager.recalculateScale();

        Component component = Component.text("Set pos to ", NamedTextColor.GRAY)
                .append(Component.text(plugin.zoomManager.x + " " + plugin.zoomManager.y + " " + plugin.zoomManager.z, NamedTextColor.DARK_AQUA));
        sender.sendMessage(component);

        plugin.zoomManager.runProgram(sender);
        return true;
    }
}
