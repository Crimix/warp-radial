package com.black_dog20.warpradial.common.proxy;

import net.minecraft.client.Minecraft;

public class ClientProxy implements IProxy {
    @Override
    public boolean isSinglePlayer() {
        return Minecraft.getInstance().isSingleplayer();
    }
}
