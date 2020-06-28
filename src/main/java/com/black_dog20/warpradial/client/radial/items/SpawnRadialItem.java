package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportSpawn;
import com.black_dog20.warpradial.common.util.TranslationHelper;

import java.util.ArrayList;
import java.util.List;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class SpawnRadialItem extends TextRadialItem {

    public SpawnRadialItem() {
        super(TranslationHelper.translate(SPAWN));
    }

    @Override
    public void click() {
        PacketHandler.sendToServer(new PacketTeleportSpawn());
    }

    @Override
    public List<String> getTooltips() {
        List<String> tooltips = new ArrayList<String>();
        tooltips.add(TranslationHelper.translateToString(SPAWN_TOOLTIP));
        tooltips.addAll(super.getTooltips());
        return tooltips;
    }
}
