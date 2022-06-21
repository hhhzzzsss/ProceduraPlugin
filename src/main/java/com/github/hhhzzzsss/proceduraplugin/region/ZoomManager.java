package com.github.hhhzzzsss.proceduraplugin.region;

import com.github.hhhzzzsss.proceduraplugin.ProceduraPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ZoomManager {
    ProceduraPlugin plugin;
    public BigDecimal x;
    public BigDecimal y;
    public BigDecimal z;
    public double zoom;

    public String program = null;

    public ZoomManager(ProceduraPlugin plugin) {
        this.plugin = plugin;
        x = new BigDecimal(0);
        y = new BigDecimal(0);
        z = new BigDecimal(0);
        zoom = 1;
        recalculateScale();
    }

    public void recalculateScale() {
        int scale = (int) Math.ceil(-Math.log10(zoom)) + 10;
        x.setScale(scale, RoundingMode.HALF_UP);
        y.setScale(scale, RoundingMode.HALF_UP);
        z.setScale(scale, RoundingMode.HALF_UP);
    }

    public void runProgram(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "Running...");
        try {
            Process process = new ProcessBuilder("go",
                    "run",
                    plugin.getConfig().getString("generatorProgramsPath") + File.separator + "cmd" + File.separator + plugin.zoomManager.program,
                    "-x=" + x,
                    "-y=" + y,
                    "-z=" + z,
                    "-zoom=" + zoom)
                    .directory(new File(plugin.getConfig().getString("generatorProgramsPath")))
                    .redirectOutput(new File(plugin.getDataFolder(), "program_output"))
                    .redirectError(new File(plugin.getDataFolder(), "program_error"))
                    .start();
            new Thread(() -> {
                try {
                    int result = process.waitFor();
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        sender.sendMessage(ChatColor.GRAY + "Finished execution with return code " + result);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
