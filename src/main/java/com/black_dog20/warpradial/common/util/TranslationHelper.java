package com.black_dog20.warpradial.common.util;

import com.black_dog20.bml.utils.translate.ITranslation;
import com.black_dog20.bml.utils.translate.TranslationUtil;
import com.black_dog20.warpradial.WarpRadial;

public class TranslationHelper extends TranslationUtil {
    public enum Translations implements ITranslation {
        PAGE_FOOTER("radial.page_footer"),
        KEY_OPEN("key.radial.open"),
        KEY_CATEGORY("key.category"),
        SPAWN("radial.item.spawn"),
        HOME("radial.item.home"),
        PLAYER_WARPS("radial.item.player_warps"),
        PLAYER_WARPS_TOOLTIP("radial.item.player_warps.tooltip"),
        SERVER_WARPS("radial.item.server_warps"),
        SERVER_WARPS_TOOLTIP("radial.item.server_warps.tooltip"),
        DIMENSION_TOOLTOP("radial.item.dimension"),
        CURRENT_FUEL("radial.item.current_fuel"),
        FUEL_USAGE("radial.item.fuel_usage"),
        SET_HOME("msg.home.set"),
        DEL_HOME("msg.home.del"),
        SET_WARP("msg.warp.set"),
        DEL_WARP("msg.warp.del"),
        COOLDOWN_MSG("msg.cooldown"),
        COOLDOWN_MSG_2("msg.cooldown_2"),
        FUEL_MSG("msg.fuel"),
        FUEL_MSG_2("msg.fuel_2"),
        TELPORTED_TO_HOME("msg.teleport_home"),
        TELPORTED_TO_SPAWN("msg.teleport_spawn"),
        NO_HOME("msg.teleport_no_home"),
        TELPORTED_TO_WARP("msg.teleport_warp"),
        NO_WARP("msg.teleport_no_warp"),
        COULD_NOT_TELEPORT("msg.teleport_failed"),
        CONTAINS_EP("item.tooltip.contains_ep"),
        ALWAYS_EDIBLE("item.tooltip.allways_edible");

        Translations(String modId, String key) {
            this.modId = modId;
            this.key = key;
        }

        Translations(String key) {
            this.modId = WarpRadial.MOD_ID;
            this.key = key;
        }

        private final String modId;
        private final String key;

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getModId() {
            return modId;
        }
    }
}
