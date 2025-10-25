package com.gmail.bobason01.regen;

import com.gmail.bobason01.itemsadder.ItemsAdderHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RegenScheduler {
    private final Plugin plugin;
    private final ItemsAdderHook itemsAdderHook;
    private final Queue<BlockSnapshot> queue = new ConcurrentLinkedQueue<>();

    public RegenScheduler(Plugin plugin, ItemsAdderHook itemsAdderHook) {
        this.plugin = plugin;
        this.itemsAdderHook = itemsAdderHook;
        Bukkit.getScheduler().runTaskTimer(plugin, this::processQueue, 1L, 1L);
    }

    public void schedule(BlockSnapshot snap, long ticks) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> queue.add(snap), ticks);
    }

    private void processQueue() {
        int maxPerTick = 200;
        for (int i = 0; i < maxPerTick; i++) {
            BlockSnapshot snap = queue.poll();
            if (snap == null) break;

            org.bukkit.World bukkitWorld = Bukkit.getWorld(snap.world());
            if (bukkitWorld == null) continue;

            Location loc = new Location(bukkitWorld, snap.x(), snap.y(), snap.z());

            try {
                if (snap.itemsAdderId() != null && itemsAdderHook.isPresent()) {
                    itemsAdderHook.placeBlock(snap.itemsAdderId(), loc);
                } else {
                    Block block = loc.getBlock();
                    BlockData data = Bukkit.createBlockData(snap.blockDataString());
                    block.setBlockData(data, false);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to restore block at " + loc + ": " + e.getMessage());
            }
        }
    }

    public void flushAll() {
        queue.clear();
    }
}
