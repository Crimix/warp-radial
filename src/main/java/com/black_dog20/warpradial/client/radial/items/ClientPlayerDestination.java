package com.black_dog20.warpradial.client.radial.items;

import com.black_dog20.bml.client.radial.api.DrawingContext;
import com.black_dog20.bml.client.radial.items.TextRadialItem;
import com.black_dog20.bml.utils.dimension.DimensionUtil;
import com.black_dog20.warpradial.common.network.PacketHandler;
import com.black_dog20.warpradial.common.network.packets.PacketTeleportPlayerWarp;
import com.black_dog20.warpradial.common.util.TranslationHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

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
    public void drawTooltips(DrawingContext context) {
        String dimension = DimensionUtil.getFormattedDimensionName(dimensionName);
        List<String> tooltips = ImmutableList.of(TranslationHelper.translateToString(DIMENSION_TOOLTOP, dimension));
        GuiUtils.drawHoveringText(tooltips, (int)context.x, (int)context.y, context.width, context.height, -1, context.fontRenderer);
    }

    @Override
    public void click() {
        PacketHandler.sendToServer(new PacketTeleportPlayerWarp(name));
    }

}
