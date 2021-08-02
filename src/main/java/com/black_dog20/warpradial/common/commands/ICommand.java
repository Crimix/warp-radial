package com.black_dog20.warpradial.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public interface ICommand {

    void register(LiteralArgumentBuilder<CommandSourceStack> builder);

    boolean shouldBeRegistered();
}
