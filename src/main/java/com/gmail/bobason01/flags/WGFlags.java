package com.gmail.bobason01.flags;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public final class WGFlags {
    public static final StateFlag BLOCK_REGEN = new StateFlag("block-regen", false);
    public static final IntegerFlag BLOCK_REGEN_TIME = new IntegerFlag("block-regen-time");

    // 문자열 요소를 담는 SetFlag
    public static final SetFlag<String> BLOCK_REGEN_WHITELIST =
            new SetFlag<>("block-regen-whitelist", new StringFlag(null));
    public static final SetFlag<String> BLOCK_REGEN_BLACKLIST =
            new SetFlag<>("block-regen-blacklist", new StringFlag(null));

    public static void register() {
        FlagRegistry r = WorldGuard.getInstance().getFlagRegistry();
        try { r.register(BLOCK_REGEN); } catch (Exception ignored) {}
        try { r.register(BLOCK_REGEN_TIME); } catch (Exception ignored) {}
        try { r.register(BLOCK_REGEN_WHITELIST); } catch (Exception ignored) {}
        try { r.register(BLOCK_REGEN_BLACKLIST); } catch (Exception ignored) {}
    }
}
