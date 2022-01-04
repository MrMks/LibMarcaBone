package com.github.mrmks.mc.dev_tools_b.sheller;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import com.github.mrmks.mc.dev_tools_b.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class Utils {

    private interface ToOperation {
        Operation call();
    }

    private static final HashMap<String, ToOperation> map = new HashMap<>();
    static {
        map.put("s", OpSave::new);
        map.put("l", OpLoad::new);
        map.put("iv", OpIV::new);
        map.put("ist", OpIST::new);
        map.put("fg", OpFG::new);
        map.put("fs", OpFS::new);
    }

    static Operation[] bakeAll(List<String> lst) {
        Operation[] ops = new Operation[lst.size()];
        int i = 0;
        for (String str : lst) {
            String[] ary = StringUtils.split(str, ',');
            Operation op = ary.length > 0 ? map.getOrDefault(ary[0], OpNothing::new).call() : new OpNothing();
            if (!op.init(new ArraySlice<>(ary, 1))) {
                op = new OpNothing();
            }
            ops[i++] = op;
        }
        return ops;
    }

    private static class OpNothing implements Operation {
        @Override
        public Object invoke(Object stack, DataTable table) {
            return stack;
        }
    }

    private static class OpSave implements Operation {
        @Override
        public Object invoke(Object stack, DataTable table) {
            table.add(stack);
            return null;
        }
    }

    private static class OpLoad implements Operation {

        private int ip;
        @Override
        public boolean init(ArraySlice<String> slice) {
            if (slice.isEmpty()) return false;
            ip = Integer.parseInt(slice.first());
            return true;
        }

        @Override
        public Object invoke(Object stack, DataTable table) {
            return table.get(ip);
        }
    }

    private static class OpIV implements Operation {

        private int iCaller;
        private int[] iParams;
        private String mn;
        @Override
        public boolean init(ArraySlice<String> slice) {
            if (slice.size() < 2) return false;
            mn = slice.first();
            iCaller = Integer.parseInt(slice.at(1));
            iParams = new int[slice.size() - 2];
            for (int i = 2; i < slice.size(); i++) {
                iParams[i - 2] = Integer.parseInt(slice.at(i));
            }
            return true;
        }

        private Method method;
        private boolean methodF = false;
        @Override
        public Object invoke(Object stack, DataTable table) {
            if (!methodF) {
                Class<?>[] types = new Class<?>[iParams.length];
                for (int i = 0; i < types.length; i++) {
                    types[i] = (iParams[i] < 0 ? stack : table.get(i)).getClass();
                }
                Class<?> k = (iCaller < 0 ? stack : table.get(iCaller)).getClass();
                try {
                    method = k.getDeclaredMethod(mn, types);
                    method.setAccessible(true);
                } catch (Exception e) {
                    methodF = true;
                }
            }
            if (method != null) {
                Object caller = iCaller < 0 ? stack : table.get(iCaller);
                Object[] params = Arrays.stream(iParams).mapToObj(i-> i < 0 ? stack : table.get(i)).toArray();
                try {
                    return method.invoke(caller, params);
                } catch (Exception e) {
                    //ignore
                }
            }
            return null;
        }
    }

    private static class OpIST implements Operation {
        private String cn, mn;
        private int[] ip;
        @Override
        public boolean init(ArraySlice<String> slice) {
            if (slice.size() < 2) return false;
            mn = slice.first();
            cn = slice.at(1);
            ip = new int[slice.size() - 2];
            for (int i = 2; i < slice.size(); i++) ip[i - 2] = Integer.parseInt(slice.at(i));
            return true;
        }

        private Method me;
        private boolean meF = false;
        @Override
        public Object invoke(Object stack, DataTable table) {
            if (!meF) {
                meF = true;
                Class<?>[] t = new Class<?>[ip.length];
                for (int i = 0; i < t.length; ++i) t[i] = (ip[i] < 0 ? stack : table.get(ip[i])).getClass();
                try {
                    Class<?> k = Class.forName(cn);
                    me = k.getDeclaredMethod(mn, t);
                    me.setAccessible(true);
                } catch (Exception ignore) {}
            }
            if (me != null) {
                Object[] args = Arrays.stream(ip).mapToObj(i->i < 0 ? stack : table.get(i)).toArray();
                try {
                    return me.invoke(null, args);
                } catch (Exception ignore){}
            }
            return null;
        }
    }

    private static class OpFG implements Operation {

        private String fn;
        private int ic;
        @Override
        public boolean init(ArraySlice<String> slice) {
            if (slice.size() < 2) return false;
            fn = slice.first();
            ic = Integer.parseInt(slice.at(1));
            return true;
        }

        private Field fd;
        private boolean fdF = false;
        @Override
        public Object invoke(Object stack, DataTable table) {
            if (!fdF) {
                fdF = true;
                try {
                    fd = (ic < 0 ? stack : table.get(ic)).getClass().getDeclaredField(fn);
                    fd.setAccessible(true);
                } catch (Exception ignore) {}
            }
            if (fd != null) {
                try {
                    return fd.get(ic < 0 ? stack : table.get(ic));
                } catch (Exception ignore) {}
            }
            return null;
        }
    }

    private static class OpFS implements Operation {
        private String fn;
        private int ic, ip;
        @Override
        public boolean init(ArraySlice<String> slice) {
            if (slice.size() < 3) return false;
            fn = slice.first();
            ic = Integer.parseInt(slice.at(1));
            ip = Integer.parseInt(slice.at(2));
            return true;
        }

        private Field fd;
        private boolean fdF = false;
        @Override
        public Object invoke(Object stack, DataTable table) {
            if (!fdF) {
                fdF = true;
                try {
                    fd = (ic < 0 ? stack : table.get(ic)).getClass().getDeclaredField(fn);
                    fd.setAccessible(true);
                } catch (Exception ignore) {}
            }
            if (fd != null) {
                try {
                    fd.set(ic < 0 ? stack : table.get(ic), ip < 0 ? stack : table.get(ip));
                } catch (Exception ignore) {}
            }
            return null;
        }
    }
}
