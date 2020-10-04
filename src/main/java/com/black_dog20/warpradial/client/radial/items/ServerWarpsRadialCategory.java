package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.bml.client.radial.items.TextRadialCategory;
import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.bml.client.screen.ConfirmInputScreen;
import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.common.util.data.Permission;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class ServerWarpsRadialCategory extends TextRadialCategory {

    public ServerWarpsRadialCategory() {
        super(SERVER_WARPS.get());
    }

    @Override
    public List<IRadialItem> getItems() {
        return ClientDataManager.SERVER_DESTINATION.stream()
                .sorted(Comparator.comparing(ClientServerDestination::getCreated).thenComparing(ClientServerDestination::getName))
                .collect(Collectors.toList());
    }

    @Override
    public void addItem(IRadialItem item) {
        // No op
    }

    @Override
    public List<ITextComponent> getTooltips() {
        List<ITextComponent> tooltips = new ArrayList<>();
        tooltips.add(SERVER_WARPS_TOOLTIP.get());
        tooltips.addAll(super.getTooltips());
        return tooltips;
    }

    @Override
    public List<IRadialItem> getContextItems() {
        if (!ClientDataManager.getPermissionOrIsOpOrSinglePlayer(Permission.CAN_CREATE_SERVER_WARPS))
            return Collections.emptyList();

        IRadialItem add = new TextRadialItem(ADD_SERVER_WARP_TOOLTIP.get()) {
            @Override
            public void click() {
                ConfirmInputScreen screen = new ConfirmInputScreen(this::onConfirmClick, ADD_SERVER_WARP_TOOLTIP.get(TextFormatting.BOLD), ADD_MESSAGE.get());
                Minecraft.getInstance().displayGuiScreen(screen);
                screen.setButtonDelay(20);
            }

            private void onConfirmClick(boolean value, String name) {
                if (value && !StringUtils.isNullOrEmpty(name)) {
                    Minecraft.getInstance().player.sendChatMessage("/warpradial serverwarp add " + name);
                }
                Minecraft.getInstance().displayGuiScreen((Screen) null);
            }
        };
        return ImmutableList.of(add);
    }
}
