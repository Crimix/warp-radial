package com.black_dog20.warpradial;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = WarpRadial.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_HOME = "home";
    public static final String CATEGORY_SPAWN = "spawn";
    public static final String CATEGORY_SERVER_WARPS = "serverWarp";
    public static final String CATEGORY_PLAYER_WARPS = "playerWarp";
    public static final String CATEGORY_ADMIN = "admin";

    private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final int GLOBAL = 1;
    public static final int NO_COOLDOWN = 0;
    public static final int PER_CATEGORY_COOLDOWN = 2;

    public static ModConfigSpec CLIENT_CONFIG;
    public static ModConfigSpec SERVER_CONFIG;

    public static ModConfigSpec.BooleanValue RADIAL_SCROLL_INVERTED;

    public static ModConfigSpec.BooleanValue PLAYER_WARPS_ALLOWED;
    public static ModConfigSpec.BooleanValue WARP_TO_SPAWN_ALLOWED;
    public static ModConfigSpec.BooleanValue HOMES_ALLOWED;
    public static ModConfigSpec.BooleanValue SERVER_WARPS_ALLOWED;
    public static ModConfigSpec.IntValue COOLDOWN_MODE;
    public static ModConfigSpec.IntValue COOLDOWN_GLOBAL;
    public static ModConfigSpec.IntValue COOLDOWN_SPAWN_WARP;
    public static ModConfigSpec.IntValue COOLDOWN_HOME_WARP;
    public static ModConfigSpec.IntValue COOLDOWN_PLAYER_WARP;
    public static ModConfigSpec.IntValue COOLDOWN_SERVER_WARP;
    public static ModConfigSpec.BooleanValue LOG_WARPS;
    public static ModConfigSpec.BooleanValue ONLY_PERMISSION_PLAYERS_CAN_USE_MENU;
    public static ModConfigSpec.BooleanValue ONLY_PERMISSION_PLAYERS_CAN_USE_SERVER_WARPS;
    public static ModConfigSpec.BooleanValue INFORM_USER_OF_MISSING_PERMISSION;
    public static ModConfigSpec.IntValue SERVER_WARP_CREATE_PERMISSION_LEVEL;
    public static ModConfigSpec.IntValue SERVER_WARP_DELETE_PERMISSION_LEVEL;
    public static ModConfigSpec.IntValue USE_MENU_PERMISSION_LEVEL;
    public static ModConfigSpec.IntValue USE_SERVER_WARP_PERMISSION_LEVEL;
    public static ModConfigSpec.IntValue PERMISSION_COMMAND_PERMISSION_LEVEL;

    static {

        CLIENT_BUILDER.comment("General settings")
                .push(CATEGORY_GENERAL);
        RADIAL_SCROLL_INVERTED = CLIENT_BUILDER.comment("Is scrolling inverted for the radial menu")
                .define("invertedScroll", false);
        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();

        SERVER_BUILDER.comment("General settings")
                .push(CATEGORY_GENERAL);
        COOLDOWN_MODE = SERVER_BUILDER.comment("Cooldown mode", "0 = no cooldown", "1 = global cooldown", "2 = per category cooldown")
                .defineInRange("warpCooldownMode", PER_CATEGORY_COOLDOWN, 0, 2);
        COOLDOWN_GLOBAL = SERVER_BUILDER.comment("Global cooldown in ticks", "Is only used if cooldownMode = 1")
                .defineInRange("globalWarpCooldownTicks", 2400, 0, 72000);

        SERVER_BUILDER.push(CATEGORY_HOME);
        HOMES_ALLOWED = SERVER_BUILDER.comment("Can homes be created and warped to")
                .define("homesAllowed", true);
        COOLDOWN_HOME_WARP = SERVER_BUILDER.comment("Home warp cooldown in ticks", "Is only used if warpCooldownMode = 2")
                .defineInRange("homeWarpCooldownTicks", 600, 0, 72000);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push(CATEGORY_SPAWN);
        WARP_TO_SPAWN_ALLOWED = SERVER_BUILDER.comment("Can spawn be warped to")
                .define("spawnAllowed", true);
        COOLDOWN_SPAWN_WARP = SERVER_BUILDER.comment("Spawn warp cooldown in ticks", "Is only used if warpCooldownMode = 2")
                .defineInRange("spawnWarpCooldownTicks", 600, 0, 72000);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push(CATEGORY_PLAYER_WARPS);
        PLAYER_WARPS_ALLOWED = SERVER_BUILDER.comment("Can players create their own warps")
                .define("playerWarpsAllowed", true);
        COOLDOWN_PLAYER_WARP = SERVER_BUILDER.comment("Player warps cooldown in ticks", "Is only used if warpCooldownMode = 2")
                .defineInRange("playerWarpCooldownTicks", 2400, 0, 72000);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push(CATEGORY_SERVER_WARPS);
        SERVER_WARPS_ALLOWED = SERVER_BUILDER.comment("Can server warps be created and warped to")
                .define("serverWarpsAllowed", true);
        COOLDOWN_SERVER_WARP = SERVER_BUILDER.comment("Server warps cooldown in ticks", "Is only used if warpCooldownMode = 2")
                .defineInRange("serverWarpCooldownTicks", 2400, 0, 72000);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push(CATEGORY_ADMIN);
        LOG_WARPS = SERVER_BUILDER.comment("Are warp add or remove logged")
                .define("logging", true);
        ONLY_PERMISSION_PLAYERS_CAN_USE_MENU = SERVER_BUILDER.comment("Only allow ops and players with specific permission to use the radial menu")
                .define("onlyOpsMenu", false);
        ONLY_PERMISSION_PLAYERS_CAN_USE_SERVER_WARPS = SERVER_BUILDER.comment("Only allow ops and players with specific permission to teleport to server warps")
                .define("onlyOpsServerWarpTeleport", false);
        INFORM_USER_OF_MISSING_PERMISSION = SERVER_BUILDER.comment("Inform the player that they do not have the required permission to use the menu or teleport")
                .define("tellUsersOpsMenu", true);
        SERVER_WARP_CREATE_PERMISSION_LEVEL = SERVER_BUILDER.comment("The op permission level the player must have to be able to use create server warp command")
                .defineInRange("createPermissionLevel", 1, 1, 4);
        SERVER_WARP_DELETE_PERMISSION_LEVEL = SERVER_BUILDER.comment("The op permission level the player must have to be able to use delete server warp command")
                .defineInRange("deletePermissionLevel", 2, 1, 4);
        USE_MENU_PERMISSION_LEVEL = SERVER_BUILDER.comment("The op permission level the player must have to be able to use the radial menu", "If onlyOpsMenu = true")
                .defineInRange("usePermissionLevel", 1, 1, 4);
        USE_SERVER_WARP_PERMISSION_LEVEL = SERVER_BUILDER.comment("The op permission level the player must have to be able to use server warps", "If onlyOpsServerWarpTeleport=true")
                .defineInRange("useServerWarpPermissionLevel", 1, 1, 4);
        PERMISSION_COMMAND_PERMISSION_LEVEL = SERVER_BUILDER.comment("The op permission level the player must have to be able to use permission grant and revoke commands")
                .defineInRange("permissionsPermissionLevel", 2, 1, 4);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.pop();
        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    public static void loadConfig(ModConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .preserveInsertionOrder()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) {
    }


}
