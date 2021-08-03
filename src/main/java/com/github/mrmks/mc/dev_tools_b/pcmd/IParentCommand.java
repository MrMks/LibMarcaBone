package com.github.mrmks.mc.dev_tools_b.pcmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.CommandSender;

import java.util.List;

interface IParentCommand extends ICommand {
    default void addChild(ISubCommand cmd) {
        getParent().addChild(cmd);
    }

    default void addChild(ICommand cmd, String name, String... aliases) {
        getParent().addChild(cmd, name, aliases);
    }

    default boolean execute(CommandSender sender, String label, String fLabel, ArraySlice<String> slice) {
        return getParent().execute(this, sender, label, fLabel, slice);
    }

    @Override
    default List<String> complete(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        return getParent().complete(sender, label, fLabel, args);
    }

    default boolean executeSelf(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        return false;
    }

    CommandParent getParent();
}
