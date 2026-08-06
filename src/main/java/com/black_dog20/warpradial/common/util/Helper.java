package com.black_dog20.warpradial.common.util;

import com.black_dog20.warpradial.client.ClientHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class Helper {

    public static boolean isSinglePlayer() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return ClientHelper.isSinglePlayer();
        } else {
            return false;
        }
    }
}
