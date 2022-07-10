package com.github.hhhzzzsss.proceduraplugin;

import com.github.hhhzzzsss.proceduraplugin.commands.*;
import com.github.hhhzzzsss.proceduraplugin.display.BossbarHandler;
import com.github.hhhzzzsss.proceduraplugin.fawe.DumpMonitor;
import com.github.hhhzzzsss.proceduraplugin.region.RegionManager;
import com.github.hhhzzzsss.proceduraplugin.region.ZoomManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProceduraPlugin extends JavaPlugin {
    public RegionManager regionManager;
    public ZoomManager zoomManager;
    public BossbarHandler bossbarHandler;
    public DumpMonitor dumpMonitor;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        saveDefaultConfig();

        regionManager = new RegionManager(this);
        zoomManager = new ZoomManager(this);
        bossbarHandler = new BossbarHandler(this);
        dumpMonitor = new DumpMonitor(this);

        getServer().getPluginManager().registerEvents(bossbarHandler, this);
        dumpMonitor.runTaskTimer(this, 20, 20);

        getCommand("select").setExecutor(new SelectCommand(this));
        getCommand("deselect").setExecutor(new DeselectCommand(this));
        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("unregister").setExecutor(new UnregisterCommand(this));
        getCommand("setname").setExecutor(new SetNameCommand(this));
        getCommand("clearregion").setExecutor(new ClearRegionCommand(this));
        getCommand("save").setExecutor(new SaveCommand(this));
        getCommand("multisave").setExecutor(new MultiSaveCommand(this));
        getCommand("goto").setExecutor(new GotoCommand(this));
        getCommand("goto").setTabCompleter(new GotoCommand(this));
        getCommand("setgeneratorpath").setExecutor(new SetGeneratorPathCommand(this));
        getCommand("setprogram").setExecutor(new SetProgramCommand(this));
        getCommand("initposandzoom").setExecutor(new InitPosAndZoomCommand(this));
        getCommand("zoom").setExecutor(new ZoomCommand(this));
        getCommand("setcenter").setExecutor(new SetCenterCommand(this));
        getCommand("setcenterandzoom").setExecutor(new SetCenterAndZoomCommand(this));
        getCommand("shift").setExecutor(new ShiftCommand(this));
        getCommand("run").setExecutor(new RunCommand(this));

        getLogger().info("ProceduraPlugin was enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("ProceduraPlugin was disabled (but I didn't implement any disabling logic so this does nothing)");
    }
}
