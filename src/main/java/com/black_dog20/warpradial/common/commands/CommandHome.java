package com.black_dog20.warpradial.common.commands;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.PermissionHelper;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class CommandHome implements ICommand {

    @Override
    public void register(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("home")
                .requires(source -> source.hasPermissionLevel(0) && PermissionHelper.onlyOpsRuleNotActiveOrCanUse(source))
                .then(Commands.literal("set")
                        .executes(this::set))
                .then(Commands.literal("remove")
                        .executes(this::del)));
    }

    @Override
    public boolean shouldBeRegistered() {
        return Config.HOMES_ALLOWED.get();
    }

    public int set(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        World world = player.world;
        WarpDestination destination = new WarpDestination(world.func_234923_W_(), player.getPosition(), player.rotationYaw, player.rotationPitch);
        DataManager.setHome(player, destination);
        context.getSource().sendFeedback(SET_HOME.get(), Config.LOG_WARPS.get());
        return Command.SINGLE_SUCCESS;
    }

    public int del(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        DataManager.deleteHome(player);
        context.getSource().sendFeedback(DEL_HOME.get(), Config.LOG_WARPS.get());
        return Command.SINGLE_SUCCESS;
    }
}
