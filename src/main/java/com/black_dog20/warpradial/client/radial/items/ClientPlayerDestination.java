package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.bml.utils.dimension.DimensionUtil;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportPlayerWarp;
import com.black_dog20.warpradial.common.util.TranslationHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class ClientPlayerDestination extends TextRadialItem {

    private final String name;
    private final String dimensionName;

    public ClientPlayerDestination(String name, String dimensionName) {
        super(new StringTextComponent(name));
        this.name = name;
        this.dimensionName = dimensionName;
    }

    @Override
    public List<String> getTooltips() {
        List<String> tooltips = new ArrayList<String>();
        String dimension = DimensionUtil.getFormattedDimensionName(dimensionName);
        tooltips.add(TranslationHelper.translateToString(DIMENSION_TOOLTOP, dimension));
        tooltips.addAll(super.getTooltips());
        return tooltips;
    }

    @Override
    public void click() {
        PacketHandler.sendToServer(new PacketTeleportPlayerWarp(name));
    }

    @Override
    public List<IRadialItem> getContextItems() {
        IRadialItem remove = new TextRadialItem(TranslationHelper.translate(REMOVE_PLAYER_WARP_TOOLTIP)) {
            @Override
            public void click() {
                ConfirmScreen screen = new ConfirmScreen(this::onConfirmClick, TranslationHelper.translate(REMOVE_PLAYER_WARP_TOOLTIP, TextFormatting.BOLD), TranslationHelper.translate(REMOVE_MESSAGE, name));
                Minecraft.getInstance().displayGuiScreen(screen);
                screen.setButtonDelay(20);
            }

            private void onConfirmClick(boolean value) {
                if (value) {
                    Minecraft.getInstance().player.sendChatMessage("/warpradial warp remove " + name);
                }
                Minecraft.getInstance().displayGuiScreen((Screen) null);
            }
        };
        return ImmutableList.of(remove);
    }
}
