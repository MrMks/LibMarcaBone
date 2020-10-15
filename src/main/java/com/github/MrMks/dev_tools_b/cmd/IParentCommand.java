package com.github.MrMks.dev_tools_b.cmd;

public interface IParentCommand extends ICommand {
    void add(IChildCommand... children);
    IChildCommand getChild(String name);
    boolean hasChild(String name);
}
