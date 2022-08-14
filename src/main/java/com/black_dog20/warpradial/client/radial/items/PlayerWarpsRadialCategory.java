package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.bml.client.radial.items.TextRadialCategory;
import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.bml.client.screen.ConfirmInputScreen;
import com.black_dog20.warpradial.client.ClientDataManager;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class PlayerWarpsRadialCategory extends TextRadialCategory {

    public PlayerWarpsRadialCategory() {
        super(PLAYER_WARPS.get());
    }

    @Override
    public List<IRadialItem> getItems() {
        return ClientDataManager.PLAYER_DESTINATION.stream()
                .sorted(Comparator.comparing(ClientPlayerDestination::getCreated).thenComparing(ClientPlayerDestination::getName))
                .collect(Collectors.toList());
    }

    @Override
    public void addItem(IRadialItem item) {
        // No op
    }

    @Override
    public List<Component> getTooltips() {
        List<Component> tooltips = new ArrayList<>();
        tooltips.add(PLAYER_WARPS_TOOLTIP.get());
        tooltips.addAll(super.getTooltips());
        return tooltips;
    }

    @Override
    public List<IRadialItem> getContextItems() {
        IRadialItem add = new TextRadialItem(ADD_PLAYER_WARP_TOOLTIP.get()) {
            @Override
            public void click() {
                ConfirmInputScreen screen = new ConfirmInputScreen(this::onConfirmClick, ADD_PLAYER_WARP_TOOLTIP.get(ChatFormatting.BOLD), ADD_MESSAGE.get());
                Minecraft.getInstance().setScreen(screen);
                screen.setButtonDelay(20);
            }

            private void onConfirmClick(boolean value, String name) {
                if (value && !StringUtil.isNullOrEmpty(name)) {
                    Minecraft.getInstance().player.command("warpradial warp add " + name);
                }
                Minecraft.getInstance().setScreen((Screen) null);
            }
        };
        return ImmutableList.of(add);
    }
}
