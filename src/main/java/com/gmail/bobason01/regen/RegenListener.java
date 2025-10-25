package com.gmail.bobason01.regen;

import com.gmail.bobason01.flags.WGFlags;
import com.gmail.bobason01.itemsadder.ItemsAdderHook;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RegenListener implements Listener {
    private final Plugin plugin;
    private final RegenScheduler scheduler;
    private final ItemsAdderHook itemsAdderHook;

    public RegenListener(Plugin plugin, RegenScheduler scheduler, ItemsAdderHook itemsAdderHook) {
        this.plugin = plugin;
        this.scheduler = scheduler;
        this.itemsAdderHook = itemsAdderHook;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Location loc = block.getLocation();

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager manager = container.get(BukkitAdapter.adapt(loc.getWorld()));
        if (manager == null) return;

        ApplicableRegionSet set = manager.getApplicableRegions(BukkitAdapter.asBlockVector(loc));

        // block-regen 플래그 (ALLOW/DENY)
        StateFlag.State enabled = set.queryValue(null, WGFlags.BLOCK_REGEN);
        if (enabled != StateFlag.State.ALLOW) return;

        // block-regen-whitelist & blacklist 처리
        Set<String> whitelist = set.queryValue(null, WGFlags.BLOCK_REGEN_WHITELIST);
        Set<String> blacklist = set.queryValue(null, WGFlags.BLOCK_REGEN_BLACKLIST);

        BlockData data = block.getBlockData();
        String id = itemsAdderHook.getBlockId(block); // ia:customblock 같은 경우
        String identifier = (id != null ? id : data.getMaterial().name());

        if (whitelist != null && !whitelist.isEmpty() && !whitelist.contains(identifier)) {
            return; // 화이트리스트에 없으면 무시
        }
        if (blacklist != null && blacklist.contains(identifier)) {
            return; // 블랙리스트에 있으면 무시
        }

        // 시간 플래그
        Integer time = set.queryValue(null, WGFlags.BLOCK_REGEN_TIME);
        long ticks = (time != null ? time : 300) * 20L;

        BlockSnapshot snap = new BlockSnapshot(loc, data, id);
        long spread = ticks + ThreadLocalRandom.current().nextLong(0, 40);

        scheduler.schedule(snap, spread);
    }
}
