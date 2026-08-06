package com.black_dog20.warpradial.client;

import net.minecraft.client.Minecraft;

public class ClientHelper {

    public static boolean isSinglePlayer() {
        return Minecraft.getInstance().hasSingleplayerServer();
    }
}
