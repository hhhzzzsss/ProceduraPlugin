package com.github.hhhzzzsss.proceduraplugin.commands;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GotoCommand implements CommandExecutor, TabCompleter {
    public ProceduraPlugin plugin;
    public GotoCommand(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);

            String name = String.join(" ", args);
            for (Region region : plugin.regionManager.getRegions()) {
                if (region.name.equals(name)) {
                    player.teleport(new Location(
                            player.getWorld(),
                            region.xpos + region.xdim/2.,
                            region.ypos + region.ydim,
                            region.zpos + region.zdim/2.
                    ));
                    player.sendMessage(ChatColor.GRAY + "Teleporting...");
                    return true;
                }
            }

            player.sendMessage(ChatColor.GRAY + "Could not find a region by that name");
            return true;
        } else {
            sender.sendMessage("Must be an ingame player");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("goto")) {
            return null;
        }
        if (args.length == 0) {
            return null;
        }

        List<String> completions = new ArrayList<>();
        String prefix = String.join(" ", args);
        for (Region region : plugin.regionManager.getRegions()) {
            if (region.name.startsWith(prefix)) {
                String[] splitName = region.name.split(" ");
                String[] incompleteArgs = Arrays.copyOfRange(splitName, args.length-1, splitName.length);
                completions.add(String.join(" ", incompleteArgs));
            }
        }

        return completions;
    }
}
