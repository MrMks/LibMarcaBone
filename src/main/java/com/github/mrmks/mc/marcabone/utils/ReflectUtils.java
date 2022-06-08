package com.github.mrmks.mc.marcabone.utils;

import org.bukkit.Bukkit;

public class ReflectUtils {
    private static String nmsPackage;
    private static String obcPackage;

    private static boolean isInit = false;
    private static void init() {
        if (!isInit) {
            obcPackage = Bukkit.getServer().getClass().getPackage().getName();
            nmsPackage = "net.minecraft.server." + obcPackage.substring(obcPackage.lastIndexOf('.') + 1);
            isInit = true;
        }
    }

    public static Class<?> loadNMSClass(String klassName) {
        init();
        return loadClass(nmsPackage, klassName);
    }

    public static Class<?> loadOBCClass(String klassName) {
        init();
        return loadClass(obcPackage, klassName);
    }

    private static Class<?> loadClass(String pkg, String kls) {
        Class<?> ret = null;
        try {ret = Class.forName(pkg + '.' + kls);} catch (Throwable ignored){}
        return ret;
    }

}
