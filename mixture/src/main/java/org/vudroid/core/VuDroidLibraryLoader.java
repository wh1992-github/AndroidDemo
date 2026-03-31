package org.vudroid.core;
/**
 * 用于加载 Vu Droid Library 数据的加载器。
 */

public class VuDroidLibraryLoader {
    private static boolean alreadyLoaded = false;

    public static void load() {
        if (alreadyLoaded) {
            return;
        }
        System.loadLibrary("vudroid");
        alreadyLoaded = true;
    }
}
