package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface IParentCommand extends ICommand {
    default void addChild(ISubCommand cmd) {
        getParent().addChild(cmd);
    }

    default boolean execute(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> slice) {
        return getParent().execute(this, sender, label, fLabel, slice);
    }

    @Override
    default List<String> complete(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        return getParent().complete(sender, label, fLabel, args);
    }

    void noSuchCommand(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args, List<String> av);

    void displayAvailableSubs(CommandSender sender, String label, List<String> fLabel, List<String> subs);

    CommandParent getParent();
}
