package com.black_dog20.warpradial.common.proxy;

public class ServerProxy implements IProxy {
    @Override
    public boolean isSinglePlayer() {
        return false;
    }
}
