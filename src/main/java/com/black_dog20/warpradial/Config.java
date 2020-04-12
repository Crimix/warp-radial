package com.black_dog20.warpradial;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";

    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

    public static final int GLOBAL = 1;
    public static final int NO_COOLDOWN = 0;
    public static final int PER_CATEGORY_COOLDOWN = 2;

    public static ForgeConfigSpec SERVER_CONFIG;
    
    public static ForgeConfigSpec.BooleanValue PLAYER_WARPS_ALLOWED;
    public static ForgeConfigSpec.BooleanValue WARP_TO_SPAWN_ALLOWED;
    public static ForgeConfigSpec.BooleanValue HOMES_ALLOWED;
    public static ForgeConfigSpec.BooleanValue SERVER_WARPS_ALLOWED;
    public static ForgeConfigSpec.BooleanValue SPAWN_WARP_WITHOUT_FUEL;
    public static ForgeConfigSpec.BooleanValue HOME_WARP_WITHOUT_FUEL;
    public static ForgeConfigSpec.BooleanValue PLAYER_WARP_WITHOUT_FUEL;
    public static ForgeConfigSpec.BooleanValue SERVER_WARP_WITHOUT_FUEL;
    public static ForgeConfigSpec.IntValue COOLDOWN_MODE;
    public static ForgeConfigSpec.BooleanValue COOLDOWN_ONLY_WHEN_NO_FUEL;
    public static ForgeConfigSpec.IntValue COOLDOWN_GLOBAL;
    public static ForgeConfigSpec.IntValue COOLDOWN_SPAWN_WARP;
    public static ForgeConfigSpec.IntValue COOLDOWN_HOME_WARP;
    public static ForgeConfigSpec.IntValue COOLDOWN_PLAYER_WARP;
    public static ForgeConfigSpec.IntValue COOLDOWN_SERVER_WARP;

    static {

        SERVER_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        HOMES_ALLOWED = SERVER_BUILDER.comment("Can homes be created and warped to")
                .define("homesAllowed", true);
        WARP_TO_SPAWN_ALLOWED = SERVER_BUILDER.comment("Can spawn be warped to")
                .define("spawnAllowed", true);
        PLAYER_WARPS_ALLOWED = SERVER_BUILDER.comment("Can players create their own warps")
                .define("playerWarpsAllowed", true);
        SERVER_WARPS_ALLOWED = SERVER_BUILDER.comment("Can server warps be created and warped to")
                .define("serverWarpsAllowed", true);
        SPAWN_WARP_WITHOUT_FUEL = SERVER_BUILDER.comment("Can players warp to spawn without using fuel")
                .define("spawnWarpNotUseFuel", false);
        HOME_WARP_WITHOUT_FUEL = SERVER_BUILDER.comment("Can players warp to home without using fuel")
                .define("homeWarpNotUseFuel", false);
        PLAYER_WARP_WITHOUT_FUEL = SERVER_BUILDER.comment("Can players warp to player warp points without using fuel")
                .define("playerWarpNotUseFuel", false);
        SERVER_WARP_WITHOUT_FUEL = SERVER_BUILDER.comment("Can players warp to server warp points without using fuel")
                .define("serverWarpNotUseFuel", false);
        COOLDOWN_ONLY_WHEN_NO_FUEL = SERVER_BUILDER.comment("Cooldown is only used when you have not enough fuel", "This does not cost fuel")
                .define("useCooldownOnlyWhenNoFuel", true);
        COOLDOWN_MODE = SERVER_BUILDER.comment("Cooldown mode", "0 = no cooldown", "1 = global cooldown", "2 = per category cooldown")
                .defineInRange("warpCooldownMode", PER_CATEGORY_COOLDOWN, 0, 2);
        COOLDOWN_GLOBAL = SERVER_BUILDER.comment("Global cooldown in ticks", "Is only used if cooldownMode = 1")
                .defineInRange("globalWarpCooldownTicks", 2400, 0, 72000);
        COOLDOWN_SPAWN_WARP = SERVER_BUILDER.comment("Spawn warp cooldown in ticks", "Is only used if warpCooldownMode = 2")
                .defineInRange("spawnWarpCooldownTicks", 600, 0, 72000);
        COOLDOWN_HOME_WARP = SERVER_BUILDER.comment("Home warp cooldown in ticks", "Is only used if warpCooldownMode = 2")
                .defineInRange("homeWarpCooldownTicks", 600, 0, 72000);
        COOLDOWN_PLAYER_WARP = SERVER_BUILDER.comment("Player warps cooldown in ticks", "Is only used if warpCooldownMode = 2")
                .defineInRange("playerWarpCooldownTicks", 2400, 0, 72000);
        COOLDOWN_SERVER_WARP = SERVER_BUILDER.comment("Server warps cooldown in ticks", "Is only used if warpCooldownMode = 2")
                .defineInRange("serverWarpCooldownTicks", 2400, 0, 72000);
        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }


}
