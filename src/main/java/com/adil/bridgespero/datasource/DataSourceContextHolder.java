package com.adil.bridgespero.datasource;

public class DataSourceContextHolder {

    private static final ThreadLocal<String> context = new ThreadLocal<>();

    public static void setMode(String mode) {
        context.set(mode);
    }

    public static String getCurrentMode() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
