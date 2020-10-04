package com.black_dog20.warpradial.common.util.data;

import com.black_dog20.bml.utils.player.TeleportDestination;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WarpDestination extends TeleportDestination {

    private long created = 0;

    public WarpDestination(RegistryKey<World> dimension, ServerPlayerEntity player) {
        super(dimension, player);
        this.created = System.currentTimeMillis();
    }

    public WarpDestination(RegistryKey<World> dimension, BlockPos pos, float yaw, float pitch) {
        super(dimension, pos, yaw, pitch);
        this.created = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return created;
    }
}
