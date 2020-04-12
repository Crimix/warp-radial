package com.black_dog20.warpradial.common.datagen;

import com.black_dog20.bml.datagen.BaseLanguageProvider;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.items.ModItems;
import net.minecraft.data.DataGenerator;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class GeneratorLanguage extends BaseLanguageProvider {
    
	public GeneratorLanguage(DataGenerator gen) {
        super(gen, WarpRadial.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(WarpRadial.itemGroup.getTranslationKey(), "Warp Radial");

        add(ModItems.REDSTONE_BREAD.get(), "Redstone Bread");
        add(ModItems.LAPIS_BREAD.get(), "Lapis Bread");
        add(ModItems.ENDER_PEARL_BREAD.get(), "Ender Pearl Bread");

        // Tooltips
        addPrefixed(PAGE_FOOTER,"Page %d of %d");
        addPrefixed(KEY_CATEGORY,"Warp Radial");
        addPrefixed(KEY_OPEN,"Open Warp Radial");
        addPrefixed(SPAWN,"Spawn");
        addPrefixed(HOME,"Home");
        addPrefixed(PLAYER_WARPS,"Player Warps");
        addPrefixed(PLAYER_WARPS_TOOLTIP,"Click me to see your player wraps");
        addPrefixed(SERVER_WARPS,"Server Warps");
        addPrefixed(SERVER_WARPS_TOOLTIP,"Click me to see the servers wraps");
        addPrefixed(DIMENSION_TOOLTOP,"Dimension: %s");
        addPrefixed(FUEL_USAGE,"Uses %f ep to teleport");
        addPrefixed(CURRENT_FUEL,"Current ep: %f");
        addPrefixed(SET_HOME,"Home set!");
        addPrefixed(DEL_HOME,"Home removed!");
        addPrefixed(SET_WARP,"Warp point %s added!");
        addPrefixed(DEL_WARP,"Warp point %s removed!");
        addPrefixed(COOLDOWN_MSG, "Could not teleport, you are on cooldown");
        addPrefixed(COOLDOWN_MSG_2, "Remaining cooldown: %ds");
        addPrefixed(FUEL_MSG, "Could not teleport, you do not have enough ep");
        addPrefixed(FUEL_MSG_2, "Remaining ep: %d, cost 1.0");
        addPrefixed(TELPORTED_TO_HOME, "Teleported home!");
        addPrefixed(TELPORTED_TO_SPAWN, "Teleported to spawn!");
        addPrefixed(NO_HOME, "No home to teleport to!");
        addPrefixed(TELPORTED_TO_WARP, "Teleported to %s!");
        addPrefixed(NO_WARP, "Warp point %s does not exist!");
        addPrefixed(COULD_NOT_TELEPORT, "Failed to teleport!");
        addPrefixed(CONTAINS_EP, "Contains %d Ender Power");
        addPrefixed(ALWAYS_EDIBLE, "Always Edible");
    }
}
