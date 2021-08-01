package com.github.mrmks.mc.dev_tools_b.pcmd;

public interface ISubCommand extends IFunctionCommand {
    String getName();
    String getDescription();
    String getUsage();
    String getPermission();
    String getPermissionMessage();
    String[] getAliases();

    void addChild(ISubCommand cmd);
    void addChild(IFunctionCommand cmd, String name, String... names);
}
