package com.github.hhhzzzsss.proceduraplugin.fawe;

import com.fastasyncworldedit.core.command.SuggestInputParseException;
import com.github.hhhzzzsss.proceduraplugin.region.Region;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
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
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class DumpLoader implements Runnable {
    private boolean finished = false;
    JavaPlugin plugin;
    private Path dumpPath;
    private Region region;
    private World world;

    public DumpLoader(JavaPlugin plugin, Path dumpPath, Region region, World world) {
        this.plugin = plugin;
        this.dumpPath = dumpPath;
        this.region = region;
        this.world = world;
    }

    @Override
    public void run() {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(dumpPath.toFile());
            BufferedInputStream bin = new BufferedInputStream(fin);
            DataInputStream din = new DataInputStream(bin);

            loadFromStream(din);

            din.close();
            Files.delete(dumpPath);
            Bukkit.getScheduler().runTask(plugin, () -> {
                Component component = Component.text("Finished loading region dump").color(NamedTextColor.GRAY);
                Bukkit.broadcast(component);
            });
        } catch (SuggestInputParseException e) {
            e.printStackTrace();
            Bukkit.getScheduler().runTask(plugin, () -> {
                Component component = Component.text("Invalid block used in region dump palette. Deleting.").color(NamedTextColor.RED);
                Bukkit.broadcast(component);
            });
            moveFailedRegionDump(fin);
        } catch (InvalidDimensionException e) {
            e.printStackTrace();
            Bukkit.getScheduler().runTask(plugin, () -> {
                Component component = Component.text(e.getMessage()).color(NamedTextColor.RED);
                Bukkit.broadcast(component);
            });
            moveFailedRegionDump(fin);
        } catch (Throwable e) {
            e.printStackTrace();
            Bukkit.getScheduler().runTask(plugin, () -> {
                Component component = Component.text("Error while loading region dump. See console for mode details").color(NamedTextColor.RED);
                Bukkit.broadcast(component);
            });
            moveFailedRegionDump(fin);
        }  finally {
            finished = true;
        }
    }

    synchronized public boolean isFinished() {
        return finished;
    }

    private void loadFromStream(DataInputStream din) throws Exception {
        int dumpXDim = din.readInt();
        int dumpYDim = din.readInt();
        int dumpZDim = din.readInt();
        if (dumpXDim != region.xdim || dumpYDim != region.ydim || dumpZDim != region.zdim) {
            throw new InvalidDimensionException(String.format(
                    "Invalid region dimension (Expected %d,%d,%d but got %d,%d,%d)",
                    region.xdim, region.ydim, region.zdim,
                    dumpXDim, dumpYDim, dumpZDim
            ));
        }
        int paletteSize = din.readInt();
        ArrayList<BlockState> palette = new ArrayList<>();
        for (int i = 0; i < paletteSize; i++) {
            int stringSize = din.readInt();
            String paletteString = new String(din.readNBytes(stringSize));
            palette.add(BlockState.get(paletteString));
        }
        try (EditSession editSession =
                     WorldEdit.getInstance()
                             .newEditSessionBuilder()
                             .world(world)
                             .fastMode(true)
                             .build()) {
            for (int y = 0; y < region.ydim; y++) {
                for (int z = 0; z < region.zdim; z++) {
                    for (int x = 0; x < region.xdim; x++) {
                        BlockState blockState = palette.get(din.readInt());
                        editSession.setBlock(x + region.xpos, y + region.ypos, z + region.zpos, blockState);
                    }
                }
            }
        }
    }

    private class InvalidDimensionException extends Exception {
        public InvalidDimensionException(String message) {
            super(message);
        }
    }

    private void moveFailedRegionDump(FileInputStream fin) {
        try {
            if (fin != null) {
                fin.close();
            }
            Files.move(dumpPath, dumpPath.resolveSibling(dumpPath.getFileName() + "_FAILED"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
