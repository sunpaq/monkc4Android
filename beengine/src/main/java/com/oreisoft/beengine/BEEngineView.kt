package com.oreisoft.beengine

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

import javax.microedition.khronos.opengles.GL10

/**
 * Created by sunyuli on 2016/12/11.
 */

open class BEEngineView(context: Context) : GLSurfaceView(context) {

    private val TAG = "BEEngineView"
    private val DEBUG = true

    private val mScaleDetector: ScaleGestureDetector
    private val mScrollDetector: GestureDetector
    private var mScaleFactor = 1f

    init {
        if (context.applicationContext.assets != null) {
            BERenderer.setAssetManager(context.applicationContext.assets)
        } else {
            error("BEEngineView - context.applicationContext.assets is null can not setAssetManager")
        }

        // Pick an EGLConfig with RGBA8 color, 24-bit depth, no stencil,
        // supporting OpenGL ES 3.0 or later backwards-compatible versions.
        setEGLContextClientVersion(3)
        setEGLConfigChooser(8, 8, 8, 8, 24, 0)
        setRenderer(Renderer(context))
        //setRenderMode(RENDERMODE_CONTINUOUSLY);//RENDERMODE_CONTINUOUSLY

        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mScrollDetector = GestureDetector(context, ScrollListener())
    }

    constructor(context: Context, attrs: AttributeSet) : this(context) {}

    override fun onTouchEvent(e: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(e)
        mScrollDetector.onTouchEvent(e)
        BERenderer.onGestureScale(mScaleFactor)
        invalidate()
        return true
    }

    fun hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        this.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    /**
     * call this method after container Activity onWindowFocusChanged() called
     * */
    fun loadModel(named: String) {
        BERenderer.openFile(named)
    }

    private class Renderer(private val context: Context) : GLSurfaceView.Renderer {
        private val TAG = "BEEngineView.Renderer"

        override fun onDrawFrame(gl: GL10) {
            BERenderer.render()
            gl.glFlush()
        }

        override fun onSurfaceCreated(gl10: GL10, eglConfig: javax.microedition.khronos.egl.EGLConfig) {
            BERenderer.init()
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            Log.v(TAG, "resize to " + width + "x" + height)
            BERenderer.resize(width, height)
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor = detector.scaleFactor
            return true
        }
    }

    private inner class ScrollListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            BERenderer.onGestureScroll(distanceX.toDouble(), distanceY.toDouble())
            return true
        }
    }

}
