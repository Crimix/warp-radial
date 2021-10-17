package com.black_dog20.warpradial.common.util;

import com.black_dog20.warpradial.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;

public class WarpPlayerProperties {

    public enum Cooldown {
        GLOBAL(Config.GLOBAL, GLOBAL_COOLDOWN, Config.COOLDOWN_GLOBAL),
        SPAWN(Config.PER_CATEGORY_COOLDOWN, SPAWN_COOLDOWN, Config.COOLDOWN_SPAWN_WARP),
        HOME(Config.PER_CATEGORY_COOLDOWN, HOME_COOLDOWN, Config.COOLDOWN_HOME_WARP),
        PLAYER(Config.PER_CATEGORY_COOLDOWN, PLAYER_WARPS_COOLDOWN, Config.COOLDOWN_PLAYER_WARP),
        SERVER(Config.PER_CATEGORY_COOLDOWN, SERVER_WARPS_COOLDOWN, Config.COOLDOWN_SERVER_WARP);

        private final int configMode;
        private final String nbtTagKey;
        private final ForgeConfigSpec.IntValue ticks;

        Cooldown(int configModeToActive, String nbtTag, ForgeConfigSpec.IntValue cooldownTicks) {
            this.configMode = configModeToActive;
            this.nbtTagKey = nbtTag;
            this.ticks = cooldownTicks;
        }

        public int getConfigMode() {
            return configMode;
        }

        public String getTagKey() {
            return nbtTagKey;
        }

        public int getTicks() {
            return ticks.get();
        }
    }

    private static String GLOBAL_COOLDOWN = "warp-radial-global-cooldown";
    private static String HOME_COOLDOWN = "warp-radial-home-cooldown";
    private static String SPAWN_COOLDOWN = "warp-radial-spawn-cooldown";
    private static String PLAYER_WARPS_COOLDOWN = "warp-radial-player-warps-cooldown";
    private static String SERVER_WARPS_COOLDOWN = "warp-radial-fuel-server-warps-cooldown";

    public static boolean isOnGlobalCooldown(Player player, Cooldown cooldown) {
        if (Cooldown.GLOBAL == cooldown)
            throw new IllegalStateException("cooldown should not be the global one");
        return Config.COOLDOWN_MODE.get() == Cooldown.GLOBAL.getConfigMode() && getRemainingCooldown(player, Cooldown.GLOBAL) > 0;
    }

    public static boolean isOnCooldown(Player player, Cooldown cooldown) {
        if (Cooldown.GLOBAL == cooldown)
            throw new IllegalStateException("cooldown should not be the global one");
        return Config.COOLDOWN_MODE.get() == cooldown.getConfigMode() && getRemainingCooldown(player, cooldown) > 0;
    }

    public static void setCooldown(Player player, Cooldown cooldown) {
        CompoundTag compound = player.getPersistentData();
        if (Cooldown.GLOBAL != cooldown)
            compound.putLong(cooldown.getTagKey(), System.currentTimeMillis() / 1000);
        compound.putLong(GLOBAL_COOLDOWN, System.currentTimeMillis() / 1000);
    }

    public static long getCooldown(Player player, Cooldown cooldown) {
        CompoundTag compound = player.getPersistentData();
        return !compound.contains(cooldown.getTagKey()) ? 0 : compound.getLong(cooldown.getTagKey());
    }

    public static long getRemainingCooldown(Player player, Cooldown cooldown) {
        long currentTime = System.currentTimeMillis() / 1000;
        long cooldownStart = getCooldown(player, cooldown);
        long remaining = (cooldown.getTicks() / 20) - (currentTime - cooldownStart);
        return remaining < 0 ? 0 : remaining;
    }
}
