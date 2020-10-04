package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportHome;
import com.black_dog20.warpradial.common.util.TranslationHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class HomeRadialItem extends TextRadialItem {

    public HomeRadialItem() {
        super(TranslationHelper.translate(HOME));
    }

    @Override
    public void click() {
        PacketHandler.sendToServer(new PacketTeleportHome());
    }

    @Override
    public List<ITextComponent> getTooltips() {
        List<ITextComponent> tooltips = new ArrayList<>();
        tooltips.add(HOME_TOOLTIP.get());
        tooltips.addAll(super.getTooltips());
        return tooltips;
    }

    @Override
    public List<IRadialItem> getContextItems() {
        IRadialItem set = new TextRadialItem(SET_HOME_TOOLTIP.get()) {
            @Override
            public void click() {
                ConfirmScreen screen = new ConfirmScreen(this::onConfirmClick, SET_HOME_TOOLTIP.get(TextFormatting.BOLD), SET_HOME_MESSAGE.get());
                Minecraft.getInstance().displayGuiScreen(screen);
                screen.setButtonDelay(20);
            }

            private void onConfirmClick(boolean value) {
                if (value) {
                    Minecraft.getInstance().player.sendChatMessage("/warpradial home set ");
                }
                Minecraft.getInstance().displayGuiScreen((Screen) null);
            }
        };

        IRadialItem remove = new TextRadialItem(REMOVE_HOME_TOOLTIP.get()) {
            @Override
            public void click() {
                ConfirmScreen screen = new ConfirmScreen(this::onConfirmClick, REMOVE_HOME_TOOLTIP.get(TextFormatting.BOLD), REMOVE_MESSAGE.get(TranslationHelper.translateToString(HOME)));
                Minecraft.getInstance().displayGuiScreen(screen);
                screen.setButtonDelay(20);
            }

            private void onConfirmClick(boolean value) {
                if (value) {
                    Minecraft.getInstance().player.sendChatMessage("/warpradial home remove ");
                }
                Minecraft.getInstance().displayGuiScreen((Screen) null);
            }
        };
        return ImmutableList.of(set, remove);
    }
}
