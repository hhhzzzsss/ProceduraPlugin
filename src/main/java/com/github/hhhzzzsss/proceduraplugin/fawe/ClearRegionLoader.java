package com.github.hhhzzzsss.proceduraplugin.fawe;

import com.fastasyncworldedit.core.command.SuggestInputParseException;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ClearRegionLoader implements Runnable {
    private boolean finished = false;
    JavaPlugin plugin;
    private Region region;
    private World world;

    public ClearRegionLoader(JavaPlugin plugin, Region region, World world) {
        this.plugin = plugin;
        this.region = region;
        this.world = world;
    }

    @Override
    public void run() {
        try (EditSession editSession =
                     WorldEdit.getInstance()
                             .newEditSessionBuilder()
                             .world(world)
                             .fastMode(true)
                             .build()) {
            for (int y = 0; y < region.ydim; y++) {
                for (int z = 0; z < region.zdim; z++) {
                    for (int x = 0; x < region.xdim; x++) {
                        int worldY = y + region.ypos;
                        int worldZ = z + region.zpos;
                        int worldX = x + region.xpos;
                        if (worldY == 0) {
                            editSession.setBlock(worldX, worldY, worldZ, BlockTypes.BEDROCK);
                        } else if (worldY >= 1 && worldY <= 16) {
                            editSession.setBlock(worldX, worldY, worldZ, BlockTypes.STONE);
                        } else if (worldY >= 17 && worldY <= 48) {
                            editSession.setBlock(worldX, worldY, worldZ, BlockTypes.DIRT);
                        } else if (worldY == 49) {
                            editSession.setBlock(worldX, worldY, worldZ, BlockTypes.GRASS_BLOCK);
                        } else {
                            editSession.setBlock(worldX, worldY, worldZ, BlockTypes.AIR);
                        }
                    }
                }
            }
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            Component component = Component.text("Successfully cleared region").color(NamedTextColor.GRAY);
            Bukkit.broadcast(component);
        });
    }
}
