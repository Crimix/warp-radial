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
        SPAWN_TOOLTIP("radial.item.spawn.tooltip"),
        HOME("radial.item.home"),
        HOME_TOOLTIP("radial.item.home.tooltip"),
        PLAYER_WARPS("radial.item.player_warps"),
        PLAYER_WARPS_TOOLTIP("radial.item.player_warps.tooltip"),
        SERVER_WARPS("radial.item.server_warps"),
        SERVER_WARPS_TOOLTIP("radial.item.server_warps.tooltip"),
        DIMENSION_TOOLTOP("radial.item.dimension"),
        ADD_MESSAGE("radial.item.add.message"),
        ADD_PLAYER_WARP_TOOLTIP("radial.item.add_player_warp.tooltip"),
        ADD_SERVER_WARP_TOOLTIP("radial.item.add_server_warp.tooltip"),
        SET_HOME_TOOLTIP("radial.item.set_home.tooltip"),
        SET_HOME_MESSAGE("radial.item.set_home.message"),
        REMOVE_PLAYER_WARP_TOOLTIP("radial.item.remove_player_warp.tooltip"),
        REMOVE_SERVER_WARP_TOOLTIP("radial.item.remove_server_warp.tooltip"),
        REMOVE_HOME_TOOLTIP("radial.item.remove_home.tooltip"),
        REMOVE_MESSAGE("radial.item.remove.message"),
        SET_HOME("msg.home.set"),
        DEL_HOME("msg.home.del"),
        SET_WARP("msg.warp.set"),
        DEL_WARP("msg.warp.del"),
        COOLDOWN_MSG("msg.cooldown"),
        COOLDOWN_MSG_2("msg.cooldown_2"),
        TELPORTED_TO_HOME("msg.teleport_home"),
        TELPORTED_TO_SPAWN("msg.teleport_spawn"),
        NO_HOME("msg.teleport_no_home"),
        TELPORTED_TO_WARP("msg.teleport_warp"),
        NO_WARP("msg.teleport_no_warp"),
        COULD_NOT_TELEPORT("msg.teleport_failed"),
        COULD_NOT_TELEPORT_INSUFFICIENT_PERMISSION("msg.teleport_failed_permission"),
        PERMISSIONS_CAN_CREATE_ADDED("msg.can_create_server_warps_added"),
        PERMISSIONS_CAN_CREATE_REMOVED("msg.can_create_server_warps_removed"),
        PERMISSIONS_CAN_DELETE_ADDED("msg.can_delete_server_warps_added"),
        PERMISSIONS_CAN_DELETE_REMOVED("msg.can_delete_server_warps_removed"),
        PERMISSIONS_CAN_USE_ADDED("msg.can_use_added"),
        PERMISSIONS_CAN_USE_REMOVED("msg.can_use_removed"),
        PERMISSIONS_CAN_USE_SERVER_WARP_ADDED("msg.can_use_server_warp_added"),
        PERMISSIONS_CAN_USE_SERVER_WARP_REMOVED("msg.can_use_server_warp_removed"),
        DENIED_MENU_USE("msg.can_not_use"),
        ;

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
