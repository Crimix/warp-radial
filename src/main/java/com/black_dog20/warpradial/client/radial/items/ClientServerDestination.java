package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.bml.utils.dimension.DimensionUtil;
import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportServerWarp;
import com.black_dog20.warpradial.common.util.TranslationHelper;
import com.black_dog20.warpradial.common.util.data.PlayerPermissions;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class ClientServerDestination extends TextRadialItem {

    private final String name;
    private final String dimensionName;
    private final long created;

    public ClientServerDestination(String name, String dimensionName, long created) {
        super(new StringTextComponent(name));
        this.name = name;
        this.dimensionName = dimensionName;
        this.created = created;
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
        PacketHandler.sendToServer(new PacketTeleportServerWarp(name));
    }

    @Override
    public List<IRadialItem> getContextItems() {
        boolean canDelete = ClientDataManager.PLAYER_PERMISSION
                .map(PlayerPermissions::canDeleteServerWarps)
                .orElse(false);
        if (!(Minecraft.getInstance().isSingleplayer() || ClientDataManager.IS_OP || canDelete))
            return Collections.emptyList();
        IRadialItem remove = new TextRadialItem(TranslationHelper.translate(REMOVE_SERVER_WARP_TOOLTIP)) {
            @Override
            public void click() {
                ConfirmScreen screen = new ConfirmScreen(this::onConfirmClick, TranslationHelper.translate(REMOVE_SERVER_WARP_TOOLTIP, TextFormatting.BOLD), TranslationHelper.translate(REMOVE_MESSAGE, name));
                Minecraft.getInstance().displayGuiScreen(screen);
                screen.setButtonDelay(20);
            }

            private void onConfirmClick(boolean value) {
                if (value) {
                    Minecraft.getInstance().player.sendChatMessage("/warpradial serverwarp remove " + name);
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
