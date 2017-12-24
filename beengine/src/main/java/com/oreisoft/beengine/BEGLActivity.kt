package com.oreisoft.beengine

import android.app.Activity
import android.view.MotionEvent

/**
 * Created by sunyuli on 12/24/2017 AD.
 */

open class BEGLActivity: Activity() {

    var beview: BEEngineView? = null

    open fun onBEEnginePrepared() {
        //override this
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        onBEEnginePrepared()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        beview?.let {
            it.onTouchEvent(e)
        }
        return true
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

}