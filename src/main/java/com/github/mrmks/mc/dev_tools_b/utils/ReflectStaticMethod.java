package com.github.mrmks.mc.dev_tools_b.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class ReflectStaticMethod {
    private boolean v = false, f;
    private MethodHandle hd;
    private Method mt;

    public ReflectStaticMethod(Class<?> klass, String name, Class<?>rType, Class<?>... args) {
        try {
            hd = MethodHandles.lookup().findStatic(klass, name, MethodType.methodType(rType, args));
            v = f = true;
        } catch (Throwable e) {
            try {
                mt = klass.getDeclaredMethod(name, args);
                mt.setAccessible(true);
                v = true;
                f = false;
            } catch (Throwable ignored) {}
        }
    }

    public boolean isValid() {
        return v;
    }

    public Object invoke(Object... args) throws Throwable {
        return f ? hd.invokeWithArguments(args) : mt.invoke(null, args);
    }
}
