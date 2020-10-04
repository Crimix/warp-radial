package com.black_dog20.warpradial.common.util.data;

public enum Permission implements PlayerPermissions.IPermission {
    CAN_CREATE_SERVER_WARPS,
    CAN_DELETE_SERVER_WARPS,
    CAN_USE_MENU;

    @Override
    public String getName() {
        return String.format("%s:%s", "WARPS", this.toString());
    }
}
