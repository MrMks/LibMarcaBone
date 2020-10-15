package com.github.MrMks.dev_tools_b.cmd;

public interface IChildCommand extends ICommand{
    void setParent(IParentCommand parent);
    IParentCommand getParent();
    boolean hasParent();
}
