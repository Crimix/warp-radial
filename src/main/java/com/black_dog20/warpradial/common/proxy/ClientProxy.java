package com.black_dog20.warpradial.common.proxy;

import com.black_dog20.warpradial.common.util.WarpPlayerProperties;
import net.minecraft.client.Minecraft;

public class ClientProxy implements IProxy {

    @Override
    public void setFuelClient(double fuel) {
        WarpPlayerProperties.setFuel(Minecraft.getInstance().player, fuel);
    }
}
