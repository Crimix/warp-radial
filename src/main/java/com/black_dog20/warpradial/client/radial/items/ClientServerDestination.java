package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.bml.utils.dimension.DimensionUtil;
import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportServerWarp;
import com.black_dog20.warpradial.common.util.TranslationHelper;
import com.black_dog20.warpradial.common.util.data.Permission;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class ClientServerDestination extends TextRadialItem {

    private final String name;
    private final String dimensionName;
    private final long created;

    public ClientServerDestination(String name, String dimensionName, long created) {
        super(Component.literal(name));
        this.name = name;
        this.dimensionName = dimensionName;
        this.created = created;
    }

    @Override
    public List<Component> getTooltips() {
        List<Component> tooltips = new ArrayList<>();
        MutableComponent dimension = DimensionUtil.getFormattedDimensionName(dimensionName);
        tooltips.add(DIMENSION_TOOLTOP.get(dimension));
        tooltips.addAll(super.getTooltips());
        return tooltips;
    }

    @Override
    public void click() {
        PacketHandler.sendToServer(new PacketTeleportServerWarp(name));
    }

    @Override
    public List<IRadialItem> getContextItems() {
        if (!ClientDataManager.getPermissionOrIsOpOrSinglePlayer(Permission.CAN_DELETE_SERVER_WARPS))
            return Collections.emptyList();

        IRadialItem remove = new TextRadialItem(TranslationHelper.translate(REMOVE_SERVER_WARP_TOOLTIP)) {
            @Override
            public void click() {
                ConfirmScreen screen = new ConfirmScreen(this::onConfirmClick, TranslationHelper.translate(REMOVE_SERVER_WARP_TOOLTIP, ChatFormatting.BOLD), TranslationHelper.translate(REMOVE_MESSAGE, name));
                Minecraft.getInstance().setScreen(screen);
                screen.setDelay(20);
            }

            private void onConfirmClick(boolean value) {
                if (value) {
                    Minecraft.getInstance().player.chat("/warpradial serverwarp remove " + name);
                }
                Minecraft.getInstance().setScreen((Screen) null);
            }
        };
        return ImmutableList.of(remove);
    }

    public long getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }
}
