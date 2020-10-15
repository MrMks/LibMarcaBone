package com.github.MrMks.dev_tools_b.cmd;

import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * This enum is use to limit the type of CommandSender.
 * You can use {@linkplain #ANYONE} to handle it manually
 * By default, if the CommandSender is instance of {@link ProxiedCommandSender ProxiedCommandSender}
 * it will only check the type of {@link ProxiedCommandSender#getCallee()} (but
 * notice that the call of {@link org.bukkit.command.CommandSender#hasPermission(String) hasPermission()}
 * only perform on the origin sender)
 */
public enum SenderType {
    /**
     * this means do not check the type of sender, can be used to handle advance check
     */
    ANYONE,
    /**
     * this simple means nobody can use this command, it may have no use
     */
    NONE,
    /**
     * this command is available for entities, include CommandMinecarts and exclude players
     */
    ENTITY,
    /**
     * only players can use this command
     */
    PLAYER,
    /**
     * this command is available if the sender is a console
     */
    CONSOLE,
    /**
     * this command is available if the sender is a block
     */
    BLOCK,
    /**
     * this type is simply equip to ENTITY && CONSOLE && BLOCK
     */
    NON_PLAYER;

    public boolean isType(CommandSender sender) {
        while (sender instanceof ProxiedCommandSender) sender = ((ProxiedCommandSender) sender).getCallee();
        return isType(sender, this);
    }

    private boolean isType(CommandSender sender, SenderType type) {
        switch (type) {
            case CONSOLE:
                return sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender;
            case ENTITY:
                return sender instanceof Entity;
            case BLOCK:
                return sender instanceof BlockCommandSender;
            case PLAYER:
                return sender instanceof Player;
            case NON_PLAYER:
                return isType(sender, CONSOLE) || isType(sender, ENTITY) || isType(sender, BLOCK);
            case NONE:
                return false;
            case ANYONE:
            default:
                return true;
        }
    }
}
