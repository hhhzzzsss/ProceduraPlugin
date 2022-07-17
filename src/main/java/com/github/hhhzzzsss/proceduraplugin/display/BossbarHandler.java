package com.github.hhhzzzsss.proceduraplugin.display;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public class BossbarHandler implements Listener {
    ProceduraPlugin plugin;
    HashMap<UUID, BossBar> bossbarMap = new HashMap<>();

    public BossbarHandler(ProceduraPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        BossBar bossbar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        bossbarMap.put(uuid, bossbar);
        bossbar.setProgress(1.0);
        updateBossbar(player);
        bossbar.addPlayer(player);
        bossbar.setVisible(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Region regionFrom = plugin.regionManager.getContainingRegion(event.getFrom());
        Region regionTo = plugin.regionManager.getContainingRegion(event.getTo());
        if (regionFrom != regionTo) {
            updateBossbar(event.getPlayer(), regionTo);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Region regionFrom = plugin.regionManager.getContainingRegion(event.getFrom());
        Region regionTo = plugin.regionManager.getContainingRegion(event.getTo());
        if (regionFrom != regionTo) {
            updateBossbar(event.getPlayer(), regionTo);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        BossBar bossbar = bossbarMap.get(uuid);
        if (bossbar != null) {
            bossbar.removeAll();
        }
        bossbarMap.remove(bossbar);
    }

    private void updateBossbar(Player player) {
        updateBossbar(player, plugin.regionManager.getContainingRegion(player.getLocation()));
    }

    private void updateBossbar(Player player, Region region) {
        UUID uuid = player.getUniqueId();
        BossBar bossbar = bossbarMap.get(uuid);
        if (bossbar == null) {
            System.err.println("Error: bossbar for player " + player.getName() + " was null!");
            return;
        }

        BarColor color;
        String title;
        if (region == null) {
            color = BarColor.WHITE;
            title = "Unregistered Area";
        } else if (region == plugin.regionManager.activeRegion) {
            color = BarColor.BLUE;
            title = region.name;
        } else {
            color = BarColor.GREEN;
            title = region.name;
        }
        bossbar.setColor(color);
        bossbar.setTitle(ChatColor.DARK_AQUA + title);
    }

    public void updateAllBossbars() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateBossbar(player);
        }
    }
}
