package com.github.hhhzzzsss.proceduraplugin;

import com.github.hhhzzzsss.proceduraplugin.commands.*;
import com.github.hhhzzzsss.proceduraplugin.display.BossbarHandler;
import com.github.hhhzzzsss.proceduraplugin.fawe.DumpMonitor;
import com.github.hhhzzzsss.proceduraplugin.region.RegionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProceduraPlugin extends JavaPlugin {
    public RegionManager regionManager;
    public BossbarHandler bossbarHandler;
    public DumpMonitor dumpMonitor;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        regionManager = new RegionManager(this);
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
        getCommand("goto").setExecutor(new GotoCommand(this));
        getCommand("goto").setTabCompleter(new GotoCommand(this));

        getLogger().info("ProceduraPlugin was enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("ProceduraPlugin was disabled (but I didn't implement any disabling logic so this does nothing)");
    }
}
