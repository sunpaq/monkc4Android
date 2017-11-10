package com.oreisoft.sapindus;

/**
 * Created by sunyuli on 11/10/2017 AD.
 */

public class BERenderer {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static native String nativeRun();
    public static native void onAppStart();
    public static native void init();
    public static native void openFile();
    public static native void resize(int width, int height);
    public static native void render();
    public static native void setAssetManager(Object man);
    public static native void onGestureScale(float scale);
    public static native void onGestureScroll(double x, double y);
}
