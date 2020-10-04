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
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import static com.black_dog20.warpradial.common.util.TranslationHelper.Translations.*;

public class CommandPermission implements ICommand {

    @Override
    public void register(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("permission")
                .requires(source -> source.hasPermissionLevel(Config.PERMISSION_COMMAND_PERMISSION_LEVEL.get()) && !isSp())
                .then(serverWarps())
                .then(onlyOps()));
    }

    private ArgumentBuilder<CommandSource, ?> serverWarps() {
        return Commands.literal("serverwarp")
                .then(Commands.literal("create")
                        .then(Commands.argument("playerName", EntityArgument.player())
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(this::create))))
                .then(Commands.literal("delete")
                        .then(Commands.argument("playerName", EntityArgument.player())
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(this::delete))));
    }

    private ArgumentBuilder<CommandSource, ?> onlyOps() {
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

    public int create(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgument.getPlayer(context, "playerName");
        boolean bool = BoolArgumentType.getBool(context, "value");
        ITextComponent msg;
        if (bool) {
            DataManager.addPlayerPermission(player, Permission.CAN_CREATE_SERVER_WARPS);
            msg = PERMISSIONS_CAN_CREATE_ADDED.get(getPlayerNameColored(player));
        } else {
            DataManager.removePlayerPermission(player, Permission.CAN_CREATE_SERVER_WARPS);
            msg = PERMISSIONS_CAN_CREATE_REMOVED.get(getPlayerNameColored(player));
        }
        context.getSource().sendFeedback(msg, true);
        informPlayer(context, player, msg);
        return Command.SINGLE_SUCCESS;
    }

    public int delete(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgument.getPlayer(context, "playerName");
        boolean bool = BoolArgumentType.getBool(context, "value");
        ITextComponent msg;
        if (bool) {
            DataManager.addPlayerPermission(player, Permission.CAN_DELETE_SERVER_WARPS);
            msg = PERMISSIONS_CAN_DELETE_ADDED.get(getPlayerNameColored(player));
        } else {
            DataManager.removePlayerPermission(player, Permission.CAN_DELETE_SERVER_WARPS);
            msg = PERMISSIONS_CAN_DELETE_REMOVED.get(getPlayerNameColored(player));
        }
        context.getSource().sendFeedback(msg, true);
        informPlayer(context, player, msg);
        return Command.SINGLE_SUCCESS;
    }

    public int useMenu(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgument.getPlayer(context, "playerName");
        boolean bool = BoolArgumentType.getBool(context, "value");
        ITextComponent msg;
        if (bool) {
            DataManager.addPlayerPermission(player, Permission.CAN_USE_MENU);
            msg = PERMISSIONS_CAN_USE_ADDED.get(getPlayerNameColored(player));
        } else {
            DataManager.removePlayerPermission(player, Permission.CAN_USE_MENU);
            msg = PERMISSIONS_CAN_USE_REMOVED.get(getPlayerNameColored(player));
        }
        context.getSource().sendFeedback(msg, true);
        informPlayer(context, player, msg);
        return Command.SINGLE_SUCCESS;
    }

    private boolean isSp() {
        return WarpRadial.Proxy.isSinglePlayer();
    }

    private void informPlayer(CommandContext<CommandSource> context, ServerPlayerEntity player, ITextComponent msg) throws CommandSyntaxException {
        if (context.getSource().getEntity() instanceof ServerPlayerEntity) {
            PlayerEntity source = context.getSource().asPlayer();
            if (!source.equals(player)) {
                player.sendMessage(msg, Util.DUMMY_UUID);
            }
        } else {
            player.sendMessage(msg, Util.DUMMY_UUID);
        }
    }

    private ITextComponent getPlayerNameColored(ServerPlayerEntity player) {
        return TextComponentBuilder.of(player.getName())
                .format(TextFormatting.BLUE)
                .build();
    }
}
