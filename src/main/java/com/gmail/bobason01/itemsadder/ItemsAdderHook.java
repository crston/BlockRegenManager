package com.gmail.bobason01.itemsadder;

import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public final class ItemsAdderHook {
    private final boolean present;

    public ItemsAdderHook() {
        this.present = Bukkit.getPluginManager().getPlugin("ItemsAdder") != null;
    }

    public boolean isPresent() {
        return present;
    }

    public String getBlockId(Block block) {
        if (!isPresent()) return null;
        try {
            CustomBlock cb = CustomBlock.byAlreadyPlaced(block);
            return cb != null ? cb.getId() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public void placeBlock(String id, Location loc) {
        if (!isPresent()) return;
        try {
            CustomBlock.place(id, loc);
        } catch (Exception e) {
            Bukkit.getLogger().warning("[BlockRegenManager] Failed to place ItemsAdder block " + id + ": " + e.getMessage());
        }
    }
}
