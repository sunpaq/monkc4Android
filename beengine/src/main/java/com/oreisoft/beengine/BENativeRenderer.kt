package com.oreisoft.beengine

/**
 * Created by sunyuli on 11/10/2017 AD.
 */

object BENativeRenderer {
    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("native_renderer")
    }

    external fun init()
    external fun openFile(name: String)
    external fun resize(width: Int, height: Int)
    external fun render()
    external fun setAssetManager(man: Any)
    external fun onGestureScale(scale: Float)
    external fun onGestureScroll(x: Double, y: Double)
}
