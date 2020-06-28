package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.bml.client.radial.items.TextRadialCategory;
import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.bml.client.screen.ConfirmInputScreen;
import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.common.util.TranslationHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class ServerWarpsRadialCategory extends TextRadialCategory {

    public ServerWarpsRadialCategory() {
        super(TranslationHelper.translate(SERVER_WARPS));
    }

    @Override
    public List<IRadialItem> getItems() {
        return new ArrayList<IRadialItem>(ClientDataManager.SERVER_DESTINATION);
    }

    @Override
    public void addItem(IRadialItem item) {
        // No op
    }

    @Override
    public List<String> getTooltips() {
        List<String> tooltips = new ArrayList<String>();
        tooltips.add(TranslationHelper.translateToString(SERVER_WARPS_TOOLTIP));
        tooltips.addAll(super.getTooltips());
        return tooltips;
    }

    @Override
    public List<IRadialItem> getContextItems() {
        if (!(Minecraft.getInstance().isSingleplayer() || ClientDataManager.IS_OP))
            return Collections.emptyList();
        IRadialItem add = new TextRadialItem(TranslationHelper.translate(ADD_SERVER_WARP_TOOLTIP)) {
            @Override
            public void click() {
                ConfirmInputScreen screen = new ConfirmInputScreen(this::onConfirmClick, TranslationHelper.translate(ADD_SERVER_WARP_TOOLTIP, TextFormatting.BOLD), TranslationHelper.translate(ADD_MESSAGE));
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
