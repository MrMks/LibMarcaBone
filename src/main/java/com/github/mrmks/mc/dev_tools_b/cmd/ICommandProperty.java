package com.github.mrmks.mc.dev_tools_b.cmd;

import java.util.List;

public interface ICommandProperty {
    String getName();
    String getDescription();
    String getUsage();
    String getPermission();
    String getPermissionMessage();
    List<String> getAliases();
}
