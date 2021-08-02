package com.black_dog20.warpradial.common.commands;

import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.PermissionHelper;
import com.black_dog20.warpradial.common.util.data.WarpDestination;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.DEL_HOME;
import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.SET_HOME;

public class CommandHome implements ICommand {

    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("home")
                .requires(source -> source.hasPermission(0) && PermissionHelper.onlyOpsRuleNotActiveOrCanUse(source))
                .then(Commands.literal("set")
                        .executes(this::set))
                .then(Commands.literal("remove")
                        .executes(this::del)));
    }

    @Override
    public boolean shouldBeRegistered() {
        return Config.HOMES_ALLOWED.get();
    }

    public int set(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        Level world = player.level;
        WarpDestination destination = new WarpDestination(world.dimension(), player.blockPosition(), player.getYRot(), player.getXRot());
        DataManager.setHome(player, destination);
        context.getSource().sendSuccess(SET_HOME.get(), Config.LOG_WARPS.get());
        return Command.SINGLE_SUCCESS;
    }

    public int del(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        DataManager.deleteHome(player);
        context.getSource().sendSuccess(DEL_HOME.get(), Config.LOG_WARPS.get());
        return Command.SINGLE_SUCCESS;
    }
}
