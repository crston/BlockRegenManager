package com.gmail.bobason01.regen;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.Objects;

public final class BlockSnapshot {
    private final String world;
    private final int x, y, z;
    private final Material type;
    private final String blockDataString;
    private final String itemsAdderId;

    public BlockSnapshot(Location loc, BlockData data, String itemsAdderId) {
        this.world = Objects.requireNonNull(loc.getWorld()).getName();
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.type = data.getMaterial();
        this.blockDataString = data.getAsString(true);
        this.itemsAdderId = itemsAdderId;
    }

    public String world() { return world; }
    public int x() { return x; }
    public int y() { return y; }
    public int z() { return z; }
    public Material type() { return type; }
    public String blockDataString() { return blockDataString; }
    public String itemsAdderId() { return itemsAdderId; }
}
