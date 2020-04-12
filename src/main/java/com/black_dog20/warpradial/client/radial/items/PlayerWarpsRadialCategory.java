package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.api.DrawingContext;
import com.black_dog20.bml.client.radial.api.items.IRadialItem;
import com.black_dog20.bml.client.radial.items.TextRadialCategory;
import com.black_dog20.warpradial.client.ClientDataManager;
import com.black_dog20.warpradial.common.util.TranslationHelper;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class PlayerWarpsRadialCategory extends TextRadialCategory {

    public PlayerWarpsRadialCategory() {
        super(TranslationHelper.translate(PLAYER_WARPS));
    }

    @Override
    public List<IRadialItem> getItems() {
        return new ArrayList<IRadialItem>(ClientDataManager.PLAYER_DESTINATION);
    }

    @Override
    public void addItem(IRadialItem item) {
        // No op
    }

    @Override
    public void drawTooltips(DrawingContext context) {
        GuiUtils.drawHoveringText(ImmutableList.of(TranslationHelper.translateToString(PLAYER_WARPS_TOOLTIP)), (int)context.x, (int)context.y, context.width, context.height, -1, context.fontRenderer);
    }
}
