package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.bml.utils.dimension.DimensionUtil;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportPlayerWarp;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class ClientPlayerDestination extends TextRadialItem {

    private final String name;
    private final String dimensionName;
    private final long created;

    public ClientPlayerDestination(String name, String dimensionName, long created) {
        super(new StringTextComponent(name));
        this.name = name;
        this.dimensionName = dimensionName;
        this.created = created;
    }

    @Override
    public List<ITextComponent> getTooltips() {
        List<ITextComponent> tooltips = new ArrayList<>();
        TextComponent dimension = DimensionUtil.getFormattedDimensionName(dimensionName);
        tooltips.add(DIMENSION_TOOLTOP.get(dimension));
        tooltips.addAll(super.getTooltips());
        return tooltips;
    }

    @Override
    public void click() {
        PacketHandler.sendToServer(new PacketTeleportPlayerWarp(name));
    }

    @Override
    public List<IRadialItem> getContextItems() {
        IRadialItem remove = new TextRadialItem(REMOVE_PLAYER_WARP_TOOLTIP.get()) {
            @Override
            public void click() {
                ConfirmScreen screen = new ConfirmScreen(this::onConfirmClick, REMOVE_PLAYER_WARP_TOOLTIP.get(TextFormatting.BOLD), REMOVE_MESSAGE.get(name));
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

    public long getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }
}
