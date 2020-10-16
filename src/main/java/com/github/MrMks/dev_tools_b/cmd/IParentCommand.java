package com.github.MrMks.dev_tools_b.cmd;

import java.util.Collection;

public interface IParentCommand extends ICommand {
    void add(IChildCommand... children);
    IChildCommand getChild(String name);
    Collection<IChildCommand> getChildren();
    boolean hasChild(String name);
}
