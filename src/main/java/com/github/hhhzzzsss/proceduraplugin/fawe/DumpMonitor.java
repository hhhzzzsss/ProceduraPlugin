package com.github.hhhzzzsss.proceduraplugin.fawe;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.file.Files;
import java.nio.file.Path;

public class DumpMonitor extends BukkitRunnable {
    private final ProceduraPlugin plugin;
    private final Path DUMP_PATH;
    private DumpLoader dumpLoader = null;

    public DumpMonitor(ProceduraPlugin plugin) {
        this.plugin = plugin;
        DUMP_PATH = plugin.getDataFolder().toPath().resolve("PROCEDURA_REGION_DUMP");
    }

    @Override
    public void run() {
        // I'm going to assume file moving is atomic, so I won't check for incomplete file
        if (Files.exists(DUMP_PATH) &&
                plugin.regionManager.activeRegion != null &&
                (dumpLoader == null || dumpLoader.isFinished())
        ) {
            Bukkit.broadcast(Component.text("Loading region dump...").color(NamedTextColor.GRAY));
            dumpLoader = new DumpLoader(plugin, DUMP_PATH, plugin.regionManager.activeRegion, new BukkitWorld(Bukkit.getWorlds().get(0)));
            Bukkit.getScheduler().runTaskAsynchronously(plugin, dumpLoader);
        }
    }
}
