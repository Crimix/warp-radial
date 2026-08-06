package com.black_dog20.warpradial.common.network.packets;

import com.black_dog20.bml.network.messages.PacketPermission;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.util.data.PlayerPermissions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class PacketWarpRadialPermission extends PacketPermission<PlayerPermissions> {

    public static final ResourceLocation ID = new ResourceLocation(WarpRadial.MOD_ID, "send_player_permission");

    public static class Data extends PacketPermission.Data<PlayerPermissions> {

        public Data(PlayerPermissions permissions) {
            super(permissions);
        }

        public Data(FriendlyByteBuf friendlyByteBuf) {
            super(friendlyByteBuf);
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }

}
