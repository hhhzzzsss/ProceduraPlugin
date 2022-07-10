package com.github.hhhzzzsss.proceduraplugin.region;

import ch.ethz.globis.phtree.PhTreeSolid;
import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

public class RegionManager {
    private ProceduraPlugin plugin;
    private ArrayList<Region> regions;
    private final PhTreeSolid<Region> phtree;
    private final Gson gson;
    public final Path indexPath;
    public final Path schemDir;

    public Region activeRegion = null;

    public RegionManager(ProceduraPlugin plugin) {
        this.plugin = plugin;
        regions = new ArrayList<>();
        phtree = PhTreeSolid.create(2);
        gson = new Gson();
        indexPath = plugin.getDataFolder().toPath().resolve("regionIndex.json");
        schemDir = plugin.getDataFolder().toPath().resolve("Schematics");
        schemDir.toFile().mkdirs();
        try {
            loadRegionsFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRegionsFromFile() throws IOException {
        regions.clear();
        phtree.clear();

        if (Files.exists(indexPath)) {
            Reader indexReader = Files.newBufferedReader(indexPath);
            Type typeToken = new TypeToken<ArrayList<Region>>(){}.getType();
            regions = gson.fromJson(indexReader, typeToken);
            indexReader.close();
        }

        for (Region region : regions) {
            phtree.put(region.getPhEntry());
        }
    }

    public void saveRegionsToFile() throws IOException {
        Writer indexWriter = Files.newBufferedWriter(indexPath);
        indexWriter.write(gson.toJson(regions));
        indexWriter.close();
    }

    public Region getContainingRegion(Location location) {
        PhTreeSolid.PhQueryS<Region> phq = phtree.queryIntersect(new long[]{location.getBlockX(),location.getBlockZ()}, new long[]{location.getBlockX(),location.getBlockZ()});
        if (phq.hasNext()) {
            return phq.next();
        } else {
            return null;
        }
    }

    public void saveRegion(Region region, String filename, int pieces) {
        if (region == null) {
            throw new CommandException("Region is null");
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new SaveThread(region, filename, pieces));
    }

    public class SaveThread implements Runnable {
        Region region;
        String filename;
        int pieces;
        World world;

        public SaveThread(Region region, String filename, int pieces) {
            this.region = region;
            this.filename = filename;
            this.pieces = pieces;
            this.world = new BukkitWorld(Bukkit.getWorlds().get(0));
        }

        @Override
        public void run() {
            for (int i = 0; i < pieces; i++) {
                int z1 = region.zdim * i / pieces;
                int z2 = region.zdim * (i+1) / pieces;
                if (z1 == z2) {
                    continue;
                }
                Clipboard clipboard;
                try (EditSession editSession =
                             WorldEdit.getInstance()
                                     .newEditSessionBuilder()
                                     .world(world)
                                     .fastMode(true)
                                     .build()) {
                    CuboidRegion cuboidRegion = new CuboidRegion(
                            BlockVector3.at(region.xpos, region.ypos, region.zpos + z1),
                            BlockVector3.at(region.xpos + region.xdim - 1, region.ypos + region.ydim - 1, region.zpos + z2 - 1)
                    );
                    clipboard = new BlockArrayClipboard(cuboidRegion, UUID.fromString("3f797064-b9bb-588f-9337-e2deb81e8973"));
                    clipboard.setOrigin(BlockVector3.at(region.xpos - 1, 50, region.zpos - 1));

                    ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                            editSession, cuboidRegion, clipboard, cuboidRegion.getMinimumPoint()
                    );
                    forwardExtentCopy.setCopyingBiomes(false);
                    forwardExtentCopy.setCopyingEntities(false);
                    try {
                        Operations.complete(forwardExtentCopy);
                    } catch (Throwable e) {
                        throw e;
                    } finally {
                        clipboard.flush();
                    }
                }

                String partFileName = filename;
                if (pieces > 1) {
                    partFileName = partFileName + "_part" + (i+1);
                }
                if (!partFileName.endsWith(".schem")) {
                    partFileName += ".schem";
                }
                Path path = schemDir.resolve(partFileName);

                try {
                    try (ClipboardWriter writer = BuiltInClipboardFormat.FAST.getWriter(Files.newOutputStream(path))) {
                        writer.write(clipboard);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.broadcast(Component.text("Failed to save plot to schematic: " + e.getMessage(), NamedTextColor.RED));
                    });
                    return;
                }
                clipboard.close();
            }
            Bukkit.getScheduler().runTask(plugin, () -> {
                Bukkit.broadcast(Component.text("Successfully saved to schematic", NamedTextColor.GRAY));
            });
        }
    }

    public Region createRegion(int xpos, int ypos, int zpos, int xdim, int ydim, int zdim) throws IOException {
        Region region = new Region(xpos, ypos, zpos, xdim, ydim, zdim);
        PhTreeSolid.PhQueryS<Region> phq = phtree.queryIntersect(region.getPhEntry());
        if (phq.hasNext()) {
            throw new RuntimeException("The region you are trying to create intersects with " + phq.next().name);
        }
        regions.add(region);
        phtree.put(region.getPhEntry());
        saveRegionsToFile();
        activeRegion = region;
        plugin.bossbarHandler.updateAllBossbars();
        return region;
    }

    public void deleteRegion(Region region) throws IOException {
        regions.remove(region);
        phtree.remove(region.getPhEntry());
        saveRegionsToFile();
        activeRegion = null;
        plugin.bossbarHandler.updateAllBossbars();
    }

    public void setRegionName(Region region, String name) throws IOException {
        region.name = name;
        saveRegionsToFile();
        plugin.bossbarHandler.updateAllBossbars();
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }
}
