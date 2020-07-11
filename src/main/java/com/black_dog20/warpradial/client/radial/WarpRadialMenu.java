package com.black_dog20.warpradial.client.radial;

import com.black_dog20.bml.client.radial.api.AbstractRadialMenu;
import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.client.events.ClientKeyEvents;
import com.black_dog20.warpradial.client.radial.items.HomeRadialItem;
import com.black_dog20.warpradial.client.radial.items.PlayerWarpsRadialCategory;
import com.black_dog20.warpradial.client.radial.items.ServerWarpsRadialCategory;
import com.black_dog20.warpradial.client.radial.items.SpawnRadialItem;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class WarpRadialMenu extends AbstractRadialMenu {

    public WarpRadialMenu(ITextComponent title) {
        super(title, getItems());
    }

    private static List<IRadialItem> getItems() {
        List<IRadialItem> items = new ArrayList<>();
        if (Config.WARP_TO_SPAWN_ALLOWED.get())
            items.add(new SpawnRadialItem());
        if (Config.SERVER_WARPS_ALLOWED.get())
            items.add(new ServerWarpsRadialCategory());
        if (Config.HOMES_ALLOWED.get())
            items.add(new HomeRadialItem());
        if (Config.PLAYER_WARPS_ALLOWED.get())
            items.add(new PlayerWarpsRadialCategory());

        return items;
    }

    @Override
    public boolean isKeyDown() {
        return InputMappings.isKeyDown(minecraft.getMainWindow().getHandle(), ClientKeyEvents.OPEN_WRAP_RADIAL.getKey().getKeyCode());
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
}
