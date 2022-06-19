package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.github.hhhzzzsss.proceduraplugin.fawe.ClearRegionLoader;
import com.github.hhhzzzsss.proceduraplugin.fawe.DumpLoader;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearRegionCommand implements CommandExecutor {
    public ProceduraPlugin plugin;
    public ClearRegionCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be an ingame player");
            return true;
        }

        Region region = plugin.regionManager.activeRegion;
        ClearRegionLoader clearRegionLoader = new ClearRegionLoader(plugin, plugin.regionManager.activeRegion, new BukkitWorld(Bukkit.getWorlds().get(0)));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, clearRegionLoader);
        Bukkit.broadcast(Component.text("Clearing region...").color(NamedTextColor.GRAY));
        return true;
    }
}
