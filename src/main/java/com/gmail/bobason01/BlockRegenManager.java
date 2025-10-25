package com.gmail.bobason01;

import com.gmail.bobason01.flags.WGFlags;
import com.gmail.bobason01.itemsadder.ItemsAdderHook;
import com.gmail.bobason01.regen.RegenListener;
import com.gmail.bobason01.regen.RegenScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockRegenManager extends JavaPlugin {
    private RegenScheduler scheduler;
    private ItemsAdderHook itemsAdderHook;

    @Override
    public void onLoad() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            WGFlags.register();
        }
    }

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null) {
            getLogger().severe("WorldGuard required");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        itemsAdderHook = new ItemsAdderHook();
        scheduler = new RegenScheduler(this, itemsAdderHook);
        Bukkit.getPluginManager().registerEvents(new RegenListener(this, scheduler, itemsAdderHook), this);
    }

    @Override
    public void onDisable() {
        if (scheduler != null) scheduler.flushAll();
    }

    public RegenScheduler getScheduler() {
        return scheduler;
    }
}
