package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandAdaptor extends AbstractCommand implements IParentCommand, TabExecutor {

    private final CommandParent parent;
    public CommandAdaptor() {
        this(null);
    }

    public CommandAdaptor(LanguageAPI api) {
        super(api, null);
        this.parent = new CommandParent();
    }

    @Override
    public CommandParent getParent() {
        return parent;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return execute(commandSender, s, new ArrayList<>(), new ArgSlice(strings));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return complete(commandSender, s, new ArrayList<>(), new ArgSlice(strings));
    }
}
