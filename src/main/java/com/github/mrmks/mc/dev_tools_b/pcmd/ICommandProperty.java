package com.github.mrmks.mc.dev_tools_b.pcmd;

import java.util.List;

public interface ICommandProperty {
    String getName();
    String getDescription();
    String getUsage();
    String getPermission();
    String getPermissionMessage();
    List<String> getAliases();
}
