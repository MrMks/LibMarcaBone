package com.github.mrmks.mc.marcabone.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public class ReflectFieldGetter {
    private boolean v = false, f;
    private MethodHandle hd;
    private Field fd;

    public ReflectFieldGetter(Class<?> klass, String name, Class<?> type) {
        try {
            hd = MethodHandles.lookup().findGetter(klass, name, type);
            v = f = true;
        } catch (Throwable e) {
            try {
                fd = klass.getDeclaredField(name);
                fd.setAccessible(true);
                v = true;
                f = false;
            } catch (Throwable ignored) {}
        }
    }

    public boolean isValid() {
        return v;
    }

    public Object invoke(Object base) throws Throwable {
        return f ? hd.invoke(base) : fd.get(base);
    }

    public Object tryInvoke(Object base, Object _def) {
        try {
            return invoke(base);
        } catch (Throwable tr) {
            return _def;
        }
    }
}
