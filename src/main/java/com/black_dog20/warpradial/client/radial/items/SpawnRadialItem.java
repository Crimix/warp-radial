package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportSpawn;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.SPAWN;
import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.SPAWN_TOOLTIP;

public class SpawnRadialItem extends TextRadialItem {

    public SpawnRadialItem() {
        super(SPAWN.get());
    }

    @Override
    public void click() {
        PacketHandler.sendToServer(new PacketTeleportSpawn());
    }

    @Override
    public List<Component> getTooltips() {
        List<Component> tooltips = new ArrayList<>();
        tooltips.add(SPAWN_TOOLTIP.get());
        tooltips.addAll(super.getTooltips());
        return tooltips;
    }
}
