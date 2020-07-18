package com.black_dog20.warpradial.common.datagen;

import com.black_dog20.bml.datagen.BaseLanguageProvider;
import com.black_dog20.warpradial.WarpRadial;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.text.TextFormatting;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class GeneratorLanguage extends BaseLanguageProvider {

    public GeneratorLanguage(DataGenerator gen) {
        super(gen, WarpRadial.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        // Tooltips
        addPrefixed(PAGE_FOOTER, "Page %d of %d");
        addPrefixed(KEY_CATEGORY, "Warp Radial");
        addPrefixed(KEY_OPEN, "Open Warp Radial");
        addPrefixed(SPAWN, "Spawn");
        addPrefixed(SPAWN_TOOLTIP, style("Left click", TextFormatting.BLUE) + " to teleport to spawn");
        addPrefixed(HOME, "Home");
        addPrefixed(HOME_TOOLTIP, style("Left click", TextFormatting.BLUE) + " to teleport to your home");
        addPrefixed(PLAYER_WARPS, "Player Warps");
        addPrefixed(PLAYER_WARPS_TOOLTIP, style("Left click", TextFormatting.BLUE) + " to see your player wraps");
        addPrefixed(SERVER_WARPS, "Server Warps");
        addPrefixed(SERVER_WARPS_TOOLTIP, style("Left click", TextFormatting.BLUE) + " to see the servers wraps");
        addPrefixed(DIMENSION_TOOLTOP, "Dimension: %s");
        addPrefixed(ADD_MESSAGE, "Please input the name of the warp");
        addPrefixed(ADD_PLAYER_WARP_TOOLTIP, "Add player warp");
        addPrefixed(ADD_SERVER_WARP_TOOLTIP, "Add server warp");
        addPrefixed(SET_HOME_TOOLTIP, "Set home");
        addPrefixed(SET_HOME_MESSAGE, "Are you sure you want to set home?");
        addPrefixed(REMOVE_PLAYER_WARP_TOOLTIP, "Remove player warp");
        addPrefixed(REMOVE_SERVER_WARP_TOOLTIP, "Remove server warp");
        addPrefixed(REMOVE_HOME_TOOLTIP, "Remove home");
        addPrefixed(REMOVE_MESSAGE, "Are you sure you want to remove %s");
        addPrefixed(SET_HOME, "Home set!");
        addPrefixed(DEL_HOME, "Home removed!");
        addPrefixed(SET_WARP, "Warp point %s added!");
        addPrefixed(DEL_WARP, "Warp point %s removed!");
        addPrefixed(COOLDOWN_MSG, "Could not teleport, you are on cooldown");
        addPrefixed(COOLDOWN_MSG_2, "Remaining cooldown: %ds");
        addPrefixed(TELPORTED_TO_HOME, "Teleported home!");
        addPrefixed(TELPORTED_TO_SPAWN, "Teleported to spawn!");
        addPrefixed(NO_HOME, "No home to teleport to!");
        addPrefixed(TELPORTED_TO_WARP, "Teleported to %s!");
        addPrefixed(NO_WARP, "Warp point %s does not exist!");
        addPrefixed(COULD_NOT_TELEPORT, "Failed to teleport!");
        addPrefixed(PERMISSIONS_CAN_CREATE_ADDED, "Granted %s permission to create server warps");
        addPrefixed(PERMISSIONS_CAN_CREATE_REMOVED, "Reworked %s's permission to create server warps");
        addPrefixed(PERMISSIONS_CAN_DELETE_ADDED, "Granted %s permission to delete server warps");
        addPrefixed(PERMISSIONS_CAN_DELETE_REMOVED, "Reworked %s's permission to delete server warps");
    }
}
