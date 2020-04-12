package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportHome;
import com.black_dog20.warpradial.common.util.TranslationHelper;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class HomeRadialItem extends TextRadialItem {

    public HomeRadialItem() {
        super(TranslationHelper.translate(HOME));
    }

    @Override
    public void click() {
        PacketHandler.sendToServer(new PacketTeleportHome());
    }
}
