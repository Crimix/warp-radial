package com.black_dog20.warpradial.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public interface ICommand {

    void register(LiteralArgumentBuilder<CommandSource> builder);

    boolean shouldBeRegistered();
}
