package com.black_dog20.warpradial.common.commands;

import com.black_dog20.bml.utils.text.TextComponentBuilder;
import com.black_dog20.warpradial.Config;
import com.black_dog20.warpradial.WarpRadial;
import com.black_dog20.warpradial.common.util.DataManager;
import com.black_dog20.warpradial.common.util.data.Permission;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class CommandPermission implements ICommand {

    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("permission")
                .requires(source -> source.hasPermission(Config.PERMISSION_COMMAND_PERMISSION_LEVEL.get()) && !isSp())
                .then(serverWarps())
                .then(onlyOps()));
    }

    private ArgumentBuilder<CommandSourceStack, ?> serverWarps() {
        return Commands.literal("serverwarp")
                .then(Commands.literal("create")
                        .then(Commands.argument("playerName", EntityArgument.player())
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(this::create))))
                .then(Commands.literal("delete")
                        .then(Commands.argument("playerName", EntityArgument.player())
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(this::delete))))
                .then(Commands.literal("use")
                        .requires(soruce -> Config.ONLY_PERMISSION_PLAYERS_CAN_USE_SERVER_WARPS.get())
                        .then(Commands.argument("playerName", EntityArgument.player())
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(this::useServerWarp))));
    }

    private ArgumentBuilder<CommandSourceStack, ?> onlyOps() {
        return Commands.literal("canUseMenu")
                .requires(source -> Config.ONLY_PERMISSION_PLAYERS_CAN_USE_MENU.get())
                .then(Commands.argument("playerName", EntityArgument.player())
                        .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(this::useMenu)));
    }

    @Override
    public boolean shouldBeRegistered() {
        return Config.SERVER_WARPS_ALLOWED.get();
    }

    public int create(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "playerName");
        boolean bool = BoolArgumentType.getBool(context, "value");
        Component msg;
        if (bool) {
            DataManager.addPlayerPermission(player, Permission.CAN_CREATE_SERVER_WARPS);
            msg = PERMISSIONS_CAN_CREATE_ADDED.get(getPlayerNameColored(player));
        } else {
            DataManager.removePlayerPermission(player, Permission.CAN_CREATE_SERVER_WARPS);
            msg = PERMISSIONS_CAN_CREATE_REMOVED.get(getPlayerNameColored(player));
        }
        context.getSource().sendSuccess(msg, true);
        informPlayer(context, player, msg);
        return Command.SINGLE_SUCCESS;
    }

    public int delete(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "playerName");
        boolean bool = BoolArgumentType.getBool(context, "value");
        Component msg;
        if (bool) {
            DataManager.addPlayerPermission(player, Permission.CAN_DELETE_SERVER_WARPS);
            msg = PERMISSIONS_CAN_DELETE_ADDED.get(getPlayerNameColored(player));
        } else {
            DataManager.removePlayerPermission(player, Permission.CAN_DELETE_SERVER_WARPS);
            msg = PERMISSIONS_CAN_DELETE_REMOVED.get(getPlayerNameColored(player));
        }
        context.getSource().sendSuccess(msg, true);
        informPlayer(context, player, msg);
        return Command.SINGLE_SUCCESS;
    }

    public int useServerWarp(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "playerName");
        boolean bool = BoolArgumentType.getBool(context, "value");
        Component msg;
        if (bool) {
            DataManager.addPlayerPermission(player, Permission.CAN_USE_SERVER_WARP);
            msg = PERMISSIONS_CAN_USE_SERVER_WARP_ADDED.get(getPlayerNameColored(player));
        } else {
            DataManager.removePlayerPermission(player, Permission.CAN_USE_SERVER_WARP);
            msg = PERMISSIONS_CAN_USE_SERVER_WARP_REMOVED.get(getPlayerNameColored(player));
        }
        context.getSource().sendSuccess(msg, true);
        informPlayer(context, player, msg);
        return Command.SINGLE_SUCCESS;
    }

    public int useMenu(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "playerName");
        boolean bool = BoolArgumentType.getBool(context, "value");
        Component msg;
        if (bool) {
            DataManager.addPlayerPermission(player, Permission.CAN_USE_MENU);
            msg = PERMISSIONS_CAN_USE_ADDED.get(getPlayerNameColored(player));
        } else {
            DataManager.removePlayerPermission(player, Permission.CAN_USE_MENU);
            msg = PERMISSIONS_CAN_USE_REMOVED.get(getPlayerNameColored(player));
        }
        context.getSource().sendSuccess(msg, true);
        informPlayer(context, player, msg);
        return Command.SINGLE_SUCCESS;
    }

    private boolean isSp() {
        return WarpRadial.Proxy.isSinglePlayer();
    }

    private void informPlayer(CommandContext<CommandSourceStack> context, ServerPlayer player, Component msg) throws CommandSyntaxException {
        if (context.getSource().getEntity() instanceof ServerPlayer) {
            Player source = context.getSource().getPlayerOrException();
            if (!source.equals(player)) {
                player.sendMessage(msg, Util.NIL_UUID);
            }
        } else {
            player.sendMessage(msg, Util.NIL_UUID);
        }
    }

    private Component getPlayerNameColored(ServerPlayer player) {
        return TextComponentBuilder.of(player.getName())
                .format(ChatFormatting.BLUE)
                .build();
    }
}
