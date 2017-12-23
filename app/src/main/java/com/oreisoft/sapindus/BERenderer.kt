package com.oreisoft.sapindus

/**
 * Created by sunyuli on 11/10/2017 AD.
 */

object BERenderer {
    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("native_renderer")
    }

    external fun nativeRun(): String
    external fun onAppStart()
    external fun init()
    external fun openFile()
    external fun resize(width: Int, height: Int)
    external fun render()
    external fun setAssetManager(man: Any)
    external fun onGestureScale(scale: Float)
    external fun onGestureScroll(x: Double, y: Double)
}
