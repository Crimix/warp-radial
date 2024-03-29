package com.black_dog20.warpradial.client.radial;

import com.black_dog20.bml.client.radial.api.AbstractRadialMenu;
import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.bml.utils.keybinds.KeybindsUtil;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.client.keybinds.Keybinds;
import com.black_dog20.warpradial.client.radial.items.HomeRadialItem;
import com.black_dog20.warpradial.client.radial.items.PlayerWarpsRadialCategory;
import com.black_dog20.warpradial.client.radial.items.ServerWarpsRadialCategory;
import com.black_dog20.warpradial.client.radial.items.SpawnRadialItem;
import com.black_dog20.warpradial.common.util.data.Permission;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class WarpRadialMenu extends AbstractRadialMenu {

    public WarpRadialMenu(Component title) {
        super(title, getItems());
    }

    private static List<IRadialItem> getItems() {
        List<IRadialItem> items = new ArrayList<>();
        if (Config.WARP_TO_SPAWN_ALLOWED.get())
            items.add(new SpawnRadialItem());
        if (Config.SERVER_WARPS_ALLOWED.get() && canUseServerWarps())
            items.add(new ServerWarpsRadialCategory());
        if (Config.HOMES_ALLOWED.get())
            items.add(new HomeRadialItem());
        if (Config.PLAYER_WARPS_ALLOWED.get())
            items.add(new PlayerWarpsRadialCategory());

        return items;
    }

    @Override
    public boolean isKeyDown() {
        return KeybindsUtil.isKeyDown(Keybinds.openWarpMenu);
    }

    @Override
    public boolean shouldDrawCenterText() {
        return false;
    }

    @Override
    public boolean ShouldClipMouseToCircle() {
        return true;
    }

    @Override
    public boolean allowClickOutsideBounds() {
        return true;
    }

    @Override
    public float getRadiusInModifier() {
        return 1.3f;
    }

    @Override
    public boolean isScrollInverted() {
        return Config.RADIAL_SCROLL_INVERTED.get();
    }

    private static boolean canUseServerWarps() {
        return !Config.ONLY_PERMISSION_PLAYERS_CAN_USE_SERVER_WARPS.get() || ClientDataManager.getPermissionOrIsOpOrSinglePlayer(Permission.CAN_USE_SERVER_WARP);
    }
}
